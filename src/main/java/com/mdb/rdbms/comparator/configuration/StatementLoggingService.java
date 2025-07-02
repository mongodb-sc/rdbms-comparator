package com.mdb.rdbms.comparator.configuration;

import com.mongodb.bulk.BulkWriteResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StatementLoggingService {
    private static final Logger logger = LoggerFactory.getLogger(StatementLoggingService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Async("sqlLogExecutor")
    public CompletableFuture<Void> logSqlExecution(List<String> sql, Thread thread, Long millis) {
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "logs");

            // Add multiple documents to bulk operation
            for (String stmt: sql) {
                Document doc = new Document()
                        .append("threadId", thread.getId())
                        .append("threadName", thread.getName())
                        .append("millis", millis)
                        .append("message", stmt)
                        .append("source", "postgres");

                bulkOps.insert(doc);
            }

            BulkWriteResult result = bulkOps.execute();
            System.out.println("Inserted: " + result.getInsertedCount());
        } catch (Exception e) {
            logger.error("Failed to log SQL execution to MongoDB", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("mongoLogExecutor")
    public CompletableFuture<Void> logMongoExecution(BsonDocument statement, Thread thread, Long millis) {
        try {
            Document doc = new Document()
                    .append("threadId", thread.getId())
                    .append("threadName", thread.getName())
                    .append("millis", millis)
                    .append("message", statement)
                    .append("source", "mongodb");
            this.mongoTemplate.insert(statement, "logs");
        } catch (Exception e) {
            logger.error("Failed to log batch MQL executions to MongoDB", e);
        }
        return CompletableFuture.completedFuture(null);
    }
}


