package com.mdb.rdbms.comparator.configuration;


import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;


public class SqlStatementInspector implements StatementInspector {
    private static final Logger logger = LoggerFactory.getLogger(SqlStatementInspector.class);

    @Autowired
    StatementLoggingService statementLoggingService;

//    @Override
//    public String inspect(String sql) {
//        this.statementLoggingService.logSqlExecution(sql, Thread.currentThread(), System.currentTimeMillis());
//        return sql;
//    }

    private final ThreadLocal<List<String>> sqlStatements = ThreadLocal.withInitial(ArrayList::new);
    private final ThreadLocal<String> operationContext = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        List<String> statements = sqlStatements.get();
        statements.add(sql);

        String context = operationContext.get();
        if (context != null) {
            logger.debug("SQL [{}]: {}", context, sql);
        }

        return sql;
    }

    public void startOperation(String operationName) {
        operationContext.set(operationName);
        sqlStatements.get().clear();
    }

    public List<String> getCollectedSql() {
        return new ArrayList<>(sqlStatements.get());
    }

    public void endOperation() {
        List<String> statements = getCollectedSql();
        String context = operationContext.get();

        if (context != null && !statements.isEmpty()) {
            logger.info("Operation [{}] executed {} SQL statements:", context, statements.size());
            statementLoggingService.logSqlExecution(statements, Thread.currentThread(), System.currentTimeMillis());
        }

        // Clean up
        sqlStatements.get().clear();
        operationContext.remove();
    }
}