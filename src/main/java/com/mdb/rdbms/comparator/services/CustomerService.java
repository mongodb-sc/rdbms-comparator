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
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);


    @Autowired
    CustomerMongoRepository mongoRepo;

    @Autowired
    MeterRegistry registry;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerJPARepository custJpaRepo;

    @Autowired
    AddressJpaRepository addressJpaRepo;


    public Customer create(Customer customer) {

        mongoRepo.save(customer);
        customer.set_id(null);

        Address saveAddress = addressJpaRepo.save(customer.getAddress());
        customer.setAddress(saveAddress);
        return custJpaRepo.save(customer);
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
