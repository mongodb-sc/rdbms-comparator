package com.mdb.rdbms.comparator.repositories.mongo;

import com.mdb.rdbms.comparator.models.Order;
import com.mdb.rdbms.comparator.models.Product;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMongoRepository extends MongoRepository<Product, Integer> {


    @Aggregation(pipeline = {"{'$search':{'index':'default',autocomplete: {query: ?0, path: 'name'}}}"})
    List<Product> searchProducts(String searchTerm);


}
