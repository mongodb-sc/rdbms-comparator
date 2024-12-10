package com.mdb.rdbms.comparator.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.RequestContext;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.observability.MongoHandlerContext;
import org.springframework.data.mongodb.observability.MongoHandlerObservationConvention;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

import java.util.Map;
import java.util.stream.Stream;

public class MongoDBCommandCountListener extends MongoObservationCommandListener {

    long commandsRun;


    public MongoDBCommandCountListener(ObservationRegistry observationRegistry) {
        super(observationRegistry);
    }

    public MongoDBCommandCountListener(ObservationRegistry observationRegistry, ConnectionString connectionString) {
        super(observationRegistry, connectionString);
    }

    public MongoDBCommandCountListener(ObservationRegistry observationRegistry, ConnectionString connectionString, MongoHandlerObservationConvention observationConvention) {
        super(observationRegistry, connectionString, observationConvention);
    }


    @Override
    public void commandStarted(CommandStartedEvent event) {
        this.commandsRun++;

    }




    public long getCommandsRun() {
        return commandsRun;
    }

    public void setCommandsRun(long commandsRun) {
        this.commandsRun = commandsRun;
    }

    public void reset(){
        this.commandsRun = 0;
    }
}
