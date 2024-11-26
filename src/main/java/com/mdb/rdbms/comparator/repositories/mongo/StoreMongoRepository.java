package com.mdb.rdbms.comparator.repositories.mongo;

import com.mdb.rdbms.comparator.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreMongoRepository extends MongoRepository<Store, Integer> {
}
