package com.mdb.rdbms.comparator.services;

import com.mdb.rdbms.comparator.models.Query;
import com.mdb.rdbms.comparator.repositories.mongo.QueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Service
public class QueryService {

    @Autowired
    QueriesRepository repository;


    public List<Query> getQueries(String threadName, long threadId, long time) {
        long startTime = time - 30000;
        return repository.findQueries(threadName, threadId, startTime);
    }

}
