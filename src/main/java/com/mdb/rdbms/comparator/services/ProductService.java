package com.mdb.rdbms.comparator.services;

import com.mdb.rdbms.comparator.models.Product;
import com.mdb.rdbms.comparator.repositories.jpa.ProductJPARepository;
import com.mdb.rdbms.comparator.repositories.mongo.ProductMongoRepository;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    ProductJPARepository jpaRepo;

    @Autowired
    ProductMongoRepository mongoRepo;
    @Autowired
    private MongoClient mongo;

    public Product create(Product product) {
        jpaRepo.save(product);
        return mongoRepo.save(product);
    }

    public List<Product> getAllProducts(String db) {
        if (db.equals("mongodb")) {
            return mongoRepo.findAll();
        } else {
            return jpaRepo.findAll();
        }
    }



}
