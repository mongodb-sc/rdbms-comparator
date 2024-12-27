package com.mdb.rdbms.comparator.models;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

    public Response(T content, List<Metrics> metrics) {
        this.content = content;
        this.metrics = metrics;
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }

    public Response(T content){
        this.content = content;
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }

    private T content;
    private List<Metrics> metrics = new ArrayList<>();
    private String threadName;
    private long threadId;
    private long millis;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public List<Metrics> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metrics> metrics) {
        this.metrics = metrics;
    }

    public void addMetric(Metrics metric){
        this.metrics.add(metric);
    }

    public String getThreadName() {
        return threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public long getMillis() {
        return millis;
    }
}
