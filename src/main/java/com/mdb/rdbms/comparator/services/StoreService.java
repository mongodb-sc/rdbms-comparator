package com.mdb.rdbms.comparator.services;

import com.mdb.rdbms.comparator.models.Address;
import com.mdb.rdbms.comparator.models.Store;
import com.mdb.rdbms.comparator.repositories.jpa.AddressJpaRepository;
import com.mdb.rdbms.comparator.repositories.jpa.StoreJPARepository;
import com.mdb.rdbms.comparator.repositories.mongo.StoreMongoRepository;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    StoreJPARepository jpaRepo;

    @Autowired
    AddressJpaRepository addressJpaRepository;

    @Autowired
    StoreMongoRepository mongoRepo;



    public Store create(Store store){

        Address savedAddress = addressJpaRepository.save(store.getAddress());
        store.setAddress(savedAddress);
        jpaRepo.save(store);
        return mongoRepo.save(store);
    }

    public List<Store> getAllStores(String db){
        if (db.equals("mongodb")) {
            return mongoRepo.findAll();
        } else {
            return jpaRepo.findAll();
        }
    }

    public List<Store> searchStores(String db, String keyword){
        if (db.equals("mongodb")) {
            return mongoRepo.searchStores(keyword);
        } else {
            return jpaRepo.findByNameStartingWith(keyword);
        }
    }



}
