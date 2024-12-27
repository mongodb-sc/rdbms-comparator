package com.mdb.rdbms.comparator.services;

import com.mdb.rdbms.comparator.models.*;
import com.mdb.rdbms.comparator.repositories.jpa.*;
import com.mdb.rdbms.comparator.repositories.mongo.CustomerMongoRepository;
import com.mongodb.client.MongoClient;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);


    @Autowired
    CustomerMongoRepository mongoRepo;

    @Autowired
    MeterRegistry registry;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerJPARepository custJpaRepo;

    @Autowired
    AddressJpaRepository addressJpaRepo;


    public Response<Customer> create(Customer customer) {
        List<Metrics> metrics = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        mongoRepo.save(customer);
        metrics.add(new Metrics(Metrics.DB.MONGO, System.currentTimeMillis() - startTime, 1L));
        customer.set_id(null);

        Session session = entityManager.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.clear();
        Address saveAddress = addressJpaRepo.save(customer.getAddress());
        customer.setAddress(saveAddress);
        Customer cust =  custJpaRepo.save(customer);
        metrics.add(new Metrics(Metrics.DB.POSTGRES, System.currentTimeMillis() - stats.getStart().toEpochMilli(), stats.getPrepareStatementCount()));
        return new Response<>(cust, metrics);
    }


    public Customer update(Integer id, Customer customer) {
        customer.setId(id);
        Customer result = mongoRepo.save(customer);
        return result;
    }

    public Page<Customer> getCustomers(String db, CustomerSearch customerSearch, int page){
        Pageable paging = PageRequest.of(page, 100);
        if(db.equals("mongodb")) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("firstName", customerSearch.getFirstName());
            params.put("lastName", customerSearch.getLastName());
            params.put("title", customerSearch.getTitle());
            params.put("address.city", customerSearch.getCity());
            params.put("address.state", customerSearch.getState());
            params.put("address.street", customerSearch.getStreet());
            params.put("address.zip", customerSearch.getZip());
            params.values().removeIf(entry -> entry == null || entry.toString().isEmpty());

            if (customerSearch.getPhone() != null && !customerSearch.getPhone().getNumber().isEmpty()){
                HashMap<String, Object> phoneParams = new HashMap<>();
                HashMap<String, Object> subParams = new HashMap<>();
                subParams.put("number", customerSearch.getPhone().getNumber());
                subParams.put("type", customerSearch.getPhone().getType());
                phoneParams.put("$elemMatch", subParams);
                params.put("phones", phoneParams);
            }
            if (customerSearch.getEmail() != null && !customerSearch.getEmail().getEmail().isEmpty()){
                HashMap<String, Object> emailParams = new HashMap<>();
                HashMap<String, Object> subParams = new HashMap<>();
                subParams.put("email", customerSearch.getEmail().getEmail());
                subParams.put("type", customerSearch.getEmail().getType());
                emailParams.put("$elemMatch", subParams);
                params.put("emails", emailParams);
            }
            double startCount = registry.counter("queries.issued").count();
            Page<Customer> results = mongoRepo.sortCustomers(params, paging);
            double queriesCount = registry.counter("queries.issued").count() - startCount;
            return new MetricsPage<>(results, queriesCount);
        } else {

            CustomerSpecification customerSpecification = new CustomerSpecification(customerSearch);
            Session session = entityManager.unwrap(Session.class);
            Statistics stats = session.getSessionFactory().getStatistics();
            stats.clear();
            Page<Customer> results = custJpaRepo.findAll(customerSpecification, paging);
            return new MetricsPage<>(results, stats.getPrepareStatementCount());

        }
    }


    public Customer getCustomerById(Integer id, String db){
        if(db.equals("mongodb")) {
            return mongoRepo.findById(id).get();
        } else {
            return custJpaRepo.findById(id).get();
        }
    }


}
