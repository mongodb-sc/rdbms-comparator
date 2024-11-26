package com.mdb.rdbms.comparator.services;

import com.mdb.rdbms.comparator.models.Address;
import com.mdb.rdbms.comparator.models.Customer;
import com.mdb.rdbms.comparator.models.Email;
import com.mdb.rdbms.comparator.models.Phone;
import com.mdb.rdbms.comparator.repositories.jpa.AddressJpaRepository;
import com.mdb.rdbms.comparator.repositories.jpa.CustomerJPARepository;
import com.mdb.rdbms.comparator.repositories.jpa.EmailJPARepository;
import com.mdb.rdbms.comparator.repositories.jpa.PhoneJPARepository;
import com.mdb.rdbms.comparator.repositories.mongo.CustomerMongoRepository;
import com.mongodb.client.MongoClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {



    @Autowired
    CustomerMongoRepository mongoRepo;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerJPARepository custJpaRepo;

    @Autowired
    AddressJpaRepository addressJpaRepo;



    public Customer create(Customer customer) {

        Address saveAddress = addressJpaRepo.save(customer.getAddress());
        customer.setAddress(saveAddress);
        Customer result = custJpaRepo.save(customer);

        mongoRepo.save(customer);
        return result;
    }


    public Customer update(Integer id, Customer customer) {
        customer.setId(id);
        Customer result = mongoRepo.save(customer);
        return result;
    }

    public List<Customer> getCustomers(String db, String firstName, String lastName, String title, String city, String state, String street, String zip, HashMap<String, String> phones, HashMap<String, String> emails){
        if(db.equals("mongodb")) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("title", title);
            params.put("address.city", city);
            params.put("address.state", state);
            params.put("address.street", street);
            params.put("add.resszip", zip);
            params.values().removeIf(Objects::isNull);
            if (phones != null){
                HashMap<String, Object> phoneParams = new HashMap<>();
                phoneParams.put("$elemMatch", phones);
                params.put("phones", phoneParams);
            }
            if (emails != null){
                HashMap<String, Object> emailParams = new HashMap<>();
                emailParams.put("$elemMatch", emails);
                params.put("$elemMatch", emailParams);
            }
            return mongoRepo.sortCustomers(params);
        } else {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null) {
                predicates.add(cb.equal(root.get("firstName"), firstName));
            }
            if (lastName != null) {
                predicates.add(cb.equal(root.get("lastName"), lastName));
            }
            if (title != null) {
                predicates.add(cb.equal(root.get("title"), title));
            }
            if (city != null || state != null || street != null || zip != null) {
                Join<Customer, Address> addressJoin = root.join("address");
                if (city != null) {
                    predicates.add(cb.equal(addressJoin.get("city"), city));
                }
                if (state != null) {
                    predicates.add(cb.equal(addressJoin.get("state"), state));
                }
                if (street != null) {
                    predicates.add(cb.equal(addressJoin.get("street"), street));
                }
                if (zip != null) {
                    predicates.add(cb.equal(addressJoin.get("zip"), zip));
                }
            }
            if (phones != null) {
                Join<Customer, Phone> phoneJoin = root.join("phones");
                predicates.add(cb.equal(phoneJoin.get("type"), phones.get("type")));
                predicates.add(cb.equal(phoneJoin.get("number"), phones.get("number")));
            }
            if (emails != null) {
                Join<Customer, Email> emailJoin = root.join("emails");
                predicates.add(cb.equal(emailJoin.get("type"), emails.get("type")));
                predicates.add(cb.equal(emailJoin.get("email"), emails.get("email")));
            }
            query.where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(query).getResultList();
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
