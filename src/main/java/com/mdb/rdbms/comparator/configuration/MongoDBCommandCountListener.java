package com.mdb.rdbms.comparator.configuration;

import com.mongodb.event.CommandStartedEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;

public class MongoDBCommandCountListener extends MongoMetricsCommandListener {

    MeterRegistry registry;



    public MongoDBCommandCountListener(MeterRegistry registry) {
        super(registry);
        this.registry = registry;
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        super.commandStarted(event);
        this.registry.counter("queries.issued").increment();
    }

}
