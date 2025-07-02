package com.mdb.rdbms.comparator;

import com.mdb.rdbms.comparator.configuration.MongoDBCommandCountListener;
import com.mdb.rdbms.comparator.configuration.SqlStatementInspector;
import com.mdb.rdbms.comparator.configuration.StatementLoggingService;
import io.micrometer.core.instrument.MeterRegistry;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.jpa")
@EnableMongoRepositories(basePackages= "com.mdb.rdbms.comparator.repositories.mongo", queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
public class RdbmsComparatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RdbmsComparatorApplication.class, args);
    }


    @Bean
    MongoClientSettingsBuilderCustomizer mongoMetricsSynchronousContextProvider(MeterRegistry registry, @Lazy StatementLoggingService statementLoggingService) {
        return (builder) -> {
            builder.addCommandListener(new MongoDBCommandCountListener(registry, statementLoggingService));
        };
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("StatementLogging-");
        executor.initialize();
        return executor;
    }

    @Bean
    public SqlStatementInspector sqlLoggingInterceptor() {
        return new SqlStatementInspector();
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            SqlStatementInspector sqlStatementInspector) {
        return hibernateProperties -> {
            hibernateProperties.put(
                    AvailableSettings.STATEMENT_INSPECTOR,
                    sqlStatementInspector
            );
            hibernateProperties.put(AvailableSettings.FORMAT_SQL, true);
        };
    }


}
