package com.mdb.rdbms.comparator.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MetricsPage<T> extends PageImpl<T> {

    long queriesIssued = 1;


    public MetricsPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MetricsPage(List<T> content) {
        super(content);
    }

    public MetricsPage(Page<T> page, long queriesIssued) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.queriesIssued = queriesIssued;
    }

    public long getQueriesIssued() {
        return queriesIssued;
    }

    public void setQueriesIssued(long queriesIssued) {
        this.queriesIssued = queriesIssued;
    }
}
