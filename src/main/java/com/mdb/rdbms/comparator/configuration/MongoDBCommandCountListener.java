package com.mdb.rdbms.comparator.configuration;

import com.mongodb.event.CommandStartedEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MongoDBCommandCountListener extends MongoMetricsCommandListener {

    MeterRegistry registry;

    Logger logger = LogManager.getLogger(MongoDBCommandCountListener.class);

    public MongoDBCommandCountListener(MeterRegistry registry) {
        super(registry);
        this.registry = registry;
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        super.commandStarted(event);
        logger.info(event);
        logger.info(event.getCommand());
        //logger.debug(System.currentTimeMillis());

    }

}
