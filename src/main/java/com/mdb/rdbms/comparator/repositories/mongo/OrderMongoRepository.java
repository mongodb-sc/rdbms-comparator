package com.mdb.rdbms.comparator.repositories.mongo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mdb.rdbms.comparator.models.Customer;
import com.mdb.rdbms.comparator.models.Order;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface OrderMongoRepository extends MongoRepository<Order, Integer> {

    @Query(value="{'details.product_id': ?0}")
    List<Order> findOrdersByProductId(Integer product_id);

    @Query(value="?0", sort = "{'orderDate': -1, 'orderStats': 1, 'customer.lastName':  1}", fields = "{'customer.address': 0, 'customer.phones': 0, 'customers.emails': 0}")
    Page<Order> searchOrders(HashMap<String, Object> params, Pageable paging);



    @Aggregation(pipeline = {"{'$search':{'index':'default',count: {type: 'total'},'compound': {'filter': ?0 }}}","{'$skip': ?1}", "{'$limit': ?2}"})
    List<Order> searchOrdersLucene(List<Document> params, int skip, int limit);


}
