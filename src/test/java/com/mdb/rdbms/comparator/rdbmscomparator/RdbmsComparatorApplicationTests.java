package com.mdb.rdbms.comparator.rdbmscomparator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

@SpringBootTest
@EnableJpaRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.jpa")
@EnableMongoRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.mongo", queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
class RdbmsComparatorApplicationTests {

//    @Test
//    void contextLoads() {
//    }

}
