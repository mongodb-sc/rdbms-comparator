package com.mdb.rdbms.comparator.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MetricsPage<T> extends PageImpl<T> {

    double queriesIssued = 1;
    String[] queries;


    public MetricsPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MetricsPage(List<T> content) {
        super(content);
    }

    public MetricsPage(Page<T> page, double queriesIssued) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.queriesIssued = queriesIssued;
    }

    public MetricsPage(Page<T> page, double queriesIssued, String[] queries) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.queriesIssued = queriesIssued;
        this.queries = queries;
    }

    public double getQueriesIssued() {
        return queriesIssued;
    }

    public void setQueriesIssued(double queriesIssued) {
        this.queriesIssued = queriesIssued;
    }

    public String[] getQueries() {
        return queries;
    }

    public void setQueries(String[] queries) {
        this.queries = queries;
    }
}
