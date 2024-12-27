package com.mdb.rdbms.comparator.models;

public class Metrics {

    private DB db;
    private long elapsedTime;
    private long queriesIssued;

    public Metrics(DB db, long elapsedTime, long queriesIssued) {
        this.db = db;
        this.elapsedTime = elapsedTime;
        this.queriesIssued = queriesIssued;
    }

    public Metrics(){

    }


    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getQueriesIssued() {
        return queriesIssued;
    }

    public void setQueriesIssued(long queriesIssued) {
        this.queriesIssued = queriesIssued;
    }

    public enum DB {
        MONGO,
        POSTGRES

    }
}
