package com.mdb.rdbms.comparator.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
public record Query(String message, long millis) {
}
