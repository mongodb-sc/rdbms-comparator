package com.mdb.rdbms.comparator.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.security.Timestamp;
import java.sql.Time;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MetricsPage<T> extends PageImpl<T> {

    double queriesIssued = 1;
    String threadName;
    long threadId;
    long millis;


    public MetricsPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }

    public MetricsPage(List<T> content, Pageable pageable, long total, double queriesIssued) {
        super(content, pageable, total);
        this.queriesIssued = queriesIssued;
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }

    public MetricsPage(List<T> content) {
        super(content);
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }

    public MetricsPage(Page<T> page, double queriesIssued) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.queriesIssued = queriesIssued;
        this.threadName = Thread.currentThread().getName();
        this.threadId = Thread.currentThread().getId();
        this.millis = System.currentTimeMillis();
    }


    public double getQueriesIssued() {
        return queriesIssued;
    }

    public void setQueriesIssued(double queriesIssued) {
        this.queriesIssued = queriesIssued;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
