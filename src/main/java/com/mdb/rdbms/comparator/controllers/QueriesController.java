package com.mdb.rdbms.comparator.controllers;

import com.mdb.rdbms.comparator.models.Query;
import com.mdb.rdbms.comparator.models.Store;
import com.mdb.rdbms.comparator.services.QueryService;
import com.mdb.rdbms.comparator.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value="api/queries", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class QueriesController {

    @Autowired
    QueryService service;




    @GetMapping
    public List<Query> getStores(@RequestParam(name = "threadName") String threadName, @RequestParam(name="threadId") long threadId, @RequestParam(name="time") long time){
        return service.getQueries(threadName, threadId, time);
    }

}
