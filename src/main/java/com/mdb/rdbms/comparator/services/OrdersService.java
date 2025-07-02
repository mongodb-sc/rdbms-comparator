package com.mdb.rdbms.comparator.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mdb.rdbms.comparator.configuration.SqlStatementInspector;
import com.mdb.rdbms.comparator.configuration.StatementLoggingService;
import com.mdb.rdbms.comparator.models.*;
import com.mdb.rdbms.comparator.models.Order;
import com.mdb.rdbms.comparator.repositories.jpa.*;
import com.mdb.rdbms.comparator.repositories.mongo.OrderMongoRepository;
import com.mdb.rdbms.comparator.repositories.mongo.ProductMongoRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.bson.BsonArray;
import org.bson.Document;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Service
public class OrdersService {

    @Autowired
    OrderMongoRepository mongoRepo;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderJPARepository jpaRepo;

    @Autowired
    ProductJPARepository productJPARepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    StoreJPARepository storeJPARepository;

    @Autowired
    AddressJpaRepository addressJPARepository;

    @Autowired
    ProductMongoRepository productRepo;

    @Autowired
    MeterRegistry registry;

    @Autowired
    SqlStatementInspector statementInspector;

    public Response<Order> create(Order order) {
        // Lines to save Order in Mongo
        Customer customer = customerService.getCustomerById(order.getCustomer().getId(), "pg");

        List<Metrics> metrics = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        order.setCustomer(customer);
        mongoRepo.save(order);

        metrics.add(new Metrics(Metrics.DB.MONGO, System.currentTimeMillis() - startTime, 1L));
        // Lines to save Order in Postgres

        statementInspector.startOperation("Create Order");
        Session session = entityManager.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.clear();

        for (OrderDetails details: order.getDetails()){
            Product product = productJPARepository.findById(details.getProduct().getId()).get();
            details.setProduct(product);
        }

        order.setCustomer(customer);
        order.setStore(storeJPARepository.findById(order.getStore().getId()).get());
        order.setShippingAddress(customer.getAddress());

        Order pgOrder = jpaRepo.save(order);

        metrics.add(new Metrics(Metrics.DB.POSTGRES, System.currentTimeMillis() - stats.getStart().toEpochMilli(), stats.getPrepareStatementCount()));
        statementInspector.endOperation();
        return new Response<>(pgOrder, metrics);
 
    }

    public List<Order> createBatch(List<Order> orders){
        List<Order> results = jpaRepo.saveAll(orders);
        mongoRepo.saveAll(results);
        return results;
    }


    public Response<List<Order>> getRecentOrders(String db, int customerId) {
        List<Metrics> metrics = new ArrayList<>();
        statementInspector.startOperation("FindRecentOrders");
        Session session = entityManager.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.clear();
        List<Order> recentOrders = jpaRepo.findByCustomerId(customerId);
        for (Order order: recentOrders){
            order.setDetails(null);
        }
        metrics.add(new Metrics(Metrics.DB.POSTGRES, System.currentTimeMillis() - stats.getStart().toEpochMilli(), stats.getPrepareStatementCount()));
        statementInspector.endOperation();
        return new Response<>(recentOrders, metrics);
    }

    public Page<Order> getAllOrders(String db, OrderSearch orderSearch, int page){
        Pageable paging = PageRequest.of(page, 100, Sort.by("orderDate").descending());

        if (db.equals("mongodb")) {
            return mongoSearch(orderSearch, paging);
        } else {
            return this.jpaSearch( orderSearch, paging);
        }
    }


    private Page<Order> mongoSearch(OrderSearch orderSearch, Pageable paging){
        HashMap<String, Object> params = new HashMap<>();
        Page<Order> result = null;
        double startCount = registry.counter("queries.issued").count();
        if (!orderSearch.isLucene()) {
           params = this.mongoQuery(params, orderSearch);
           result = mongoRepo.searchOrders(params ,paging);
        } else {
            JsonArray jsonArray = this.mongoLucene(orderSearch);
            List<Document> query = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                query.add(Document.parse(jsonElement.toString()));
            }
            System.out.println(query.toString());
            List<Order> searchResults = mongoRepo.searchOrdersLucene(query, paging.getPageNumber(), paging.getPageSize());
            long totalRecords = mongoRepo.count();
            result = new PageImpl<>(searchResults, paging, totalRecords);
        }
        double queriesCount = registry.counter("queries.issued").count() - startCount;
        return new MetricsPage<Order>(result, queriesCount);
    }


    private Page<Order> jpaSearch(OrderSearch orderSearch, Pageable paging){
        statementInspector.startOperation("FindOrders");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);
        OrderSpecification orderSpec = new OrderSpecification(orderSearch);
        Session session = entityManager.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.clear();
        Page<Order> results = jpaRepo.findAll(orderSpec, paging);
        statementInspector.endOperation();
        return new MetricsPage<>(results, stats.getPrepareStatementCount());
    }

    private HashMap<String, Object> mongoQuery(HashMap<String, Object> params, OrderSearch orderSearch){
        try {
            for (Field field : orderSearch.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(orderSearch) != null) {
                    if ( field.getType().equals(Address.class) || field.getType().equals(Customer.class) || field.getType().equals(Store.class)) {
                        Object subObj = field.get(orderSearch);
                        for (Field subField : subObj.getClass().getDeclaredFields()) {
                            subField.setAccessible(true);
                            if (subField.get(subObj) != null && !subField.get(subObj).toString().isEmpty()) {
                                params.put(field.getName() + "." + subField.getName(), subField.get(subObj));
                            }
                        }
                    } else if (field.getType().equals(Date.class)) {
                        Instant date = ((Date) field.get(orderSearch)).toInstant();
                        HashMap<String, Object> subParams = new HashMap();
                        subParams.put("$gte", new Date(date.truncatedTo(ChronoUnit.DAYS).toEpochMilli()));
                        subParams.put("$lte", new Date(date.minus(Period.ofDays(-1)).truncatedTo(ChronoUnit.DAYS).toEpochMilli()));
                        params.put(field.getName(), subParams);
                    } else {
                        if (!field.get(orderSearch).toString().isEmpty()) {
                            params.put(field.getName(), field.get(orderSearch));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        params.remove("customer.phones");
        params.remove("customer.emails");
        params.remove("customer.location");
        params.remove("lucene");
        params.values().removeIf(Objects::isNull);
        return params;
    }

    private JsonArray mongoLucene( OrderSearch orderSearch){
        orderSearch.getCustomer().setPhones(null);
        orderSearch.getCustomer().setEmails(null);
        JsonArray json = new JsonArray();
        HashMap<String, Object> queryStringParams = new HashMap<>();
        try {
            for (Field field : orderSearch.getClass().getDeclaredFields()) {
                if (field.getName().equals("lucene")){
                    continue;
                }
                field.setAccessible(true);
                if (field.get(orderSearch) != null && (field.getType().equals(Address.class) || field.getType().equals(Customer.class) || field.getType().equals(Store.class))) {
                    Object subObj = field.get(orderSearch);
                    for (Field subField : subObj.getClass().getDeclaredFields()) {
                        subField.setAccessible(true);
                        if (subField.get(subObj) != null && !subField.get(subObj).toString().isEmpty()) {
                            if (Number.class.isAssignableFrom(subField.getType())) {
                                JsonObject equals = new JsonObject();
                                equals.addProperty("path", field.getName() + "." + subField.getName());
                                equals.addProperty("value", (Number)subField.get(subObj));
                                JsonObject entry = new JsonObject();
                                entry.add("equals", equals);
                                json.add(entry);
                            } else {
                                queryStringParams.put(field.getName() + "." + subField.getName(), subField.get(subObj));
                            }
                        }
                    }
                } else {
                    if (field.get(orderSearch) != null && !field.get(orderSearch).toString().isEmpty()) {
                        if (Number.class.isAssignableFrom(field.getType())) {
                            JsonObject equals = new JsonObject();
                            equals.addProperty("path", field.getName());
                            equals.addProperty("value", (Number)field.get(orderSearch));
                            JsonObject entry = new JsonObject();
                            entry.add("equals", equals);
                            json.add(entry);
                        } else {
                            queryStringParams.put(field.getName(), field.get(orderSearch));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        for (String key : queryStringParams.keySet()) {
            boolean firstLoop = builder.isEmpty();
            if (!firstLoop) {
                builder.append(" AND ");
            } else {
                builder.append(" ( ");
            }
            builder.append(key + " : " + queryStringParams.get(key).toString());
        }
        builder.append(" ) ");
        JsonObject queryStringElement = new JsonObject();
        queryStringElement.addProperty("defaultPath", "orderStatus");
        queryStringElement.addProperty("query", builder.toString());
        JsonObject queryString = new JsonObject();
        queryString.add("queryString", queryStringElement);
        json.add(queryString);


        return json;
    }



}
