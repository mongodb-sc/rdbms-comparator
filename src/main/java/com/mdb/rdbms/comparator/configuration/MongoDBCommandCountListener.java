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
import org.bson.BsonString;
import org.bson.Document;
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

    public MongoDBCommandCountListener(MeterRegistry registry, StatementLoggingService statementLoggingService) {
        super(registry);
        this.registry = registry;
        this.statementLoggingService = statementLoggingService;
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        super.commandStarted(event);
        if (event.getCommandName().matches("aggregate|update")) {
            this.statementLoggingService.logMongoExecution(event.getCommand().clone(), Thread.currentThread(), System.currentTimeMillis());
        } else  if (event.getCommandName().matches("find|insert")) {
            // Because these can happen against the logs collection, check for those separately so that searching for the queries doesn't create new entries
            BsonString coll = event.getCommandName().equals("find") ? event.getCommand().get("find").asString() : event.getCommand().get("insert").asString();
            if (!coll.getValue().equals("logs")) {
                this.statementLoggingService.logMongoExecution(event.getCommand().clone(), Thread.currentThread(), System.currentTimeMillis());
            }
        }
    }

}
