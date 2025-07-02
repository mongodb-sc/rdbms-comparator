package com.mdb.rdbms.comparator.repositories.mongo;

import com.mdb.rdbms.comparator.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public interface QueriesRepository extends MongoRepository<com.mdb.rdbms.comparator.models.Query, String> {


    @Query(value="{'threadName':  ?0, 'millis':  {'$gte':  ?2}}", fields = "{'_id': 0, 'message': 1, 'millis':  1, 'source': 1}", sort = "{'millis': 1}")
    List<com.mdb.rdbms.comparator.models.Query> findQueries(String threadName, long threadId, long endTime);
}
