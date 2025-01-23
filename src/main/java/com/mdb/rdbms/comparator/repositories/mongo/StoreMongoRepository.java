package com.mdb.rdbms.comparator.repositories.mongo;

import com.mdb.rdbms.comparator.models.Product;
import com.mdb.rdbms.comparator.models.Store;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoreMongoRepository extends MongoRepository<Store, Integer> {

    @Aggregation(pipeline = {"{'$search':{'index':'default',autocomplete: {query: ?0, path: 'name'}}}"})
    List<Store> searchStores(String searchTerm);
}
