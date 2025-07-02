package com.mdb.rdbms.comparator.configuration;

import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MongoDBCommandCountListener extends MongoMetricsCommandListener {

    MeterRegistry registry;
    Logger logger = LogManager.getLogger(MongoDBCommandCountListener.class);
    StatementLoggingService statementLoggingService;

    public MongoDBCommandCountListener(MeterRegistry registry) {
        super(registry);
        this.registry = registry;
        //this.statementLoggingService = statementLoggingService;
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        super.commandStarted(event);
        if (event.getCommandName().matches("find|aggregate|insert|update")) {
            this.logger.info(event.getCommand());
            //this.statementLoggingService.logMongoExecution(event.getCommand(), Thread.currentThread(), System.currentTimeMillis());
        }
    }

}
