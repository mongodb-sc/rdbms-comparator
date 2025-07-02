package com.mdb.rdbms.comparator;

import com.mdb.rdbms.comparator.configuration.MongoDBCommandCountListener;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
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


    @Bean
    MongoClientSettingsBuilderCustomizer mongoMetricsSynchronousContextProvider(MeterRegistry registry) {
        return (builder) -> {
            builder.addCommandListener(new MongoDBCommandCountListener(registry));
        };
    }


}
