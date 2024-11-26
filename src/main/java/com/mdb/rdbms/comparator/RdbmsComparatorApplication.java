package com.mdb.rdbms.comparator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

@SpringBootApplication
@EnableJpaRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.jpa")
@EnableMongoRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.mongo", queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
        public class RdbmsComparatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RdbmsComparatorApplication.class, args);
    }

}
