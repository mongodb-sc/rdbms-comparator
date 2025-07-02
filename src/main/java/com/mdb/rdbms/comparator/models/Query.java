package com.mdb.rdbms.comparator.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
public record Query(String message, long millis, String source, String location) {
    public Query(String message, long millis, String source){
        this(message, millis, source, "US");
    }
}
