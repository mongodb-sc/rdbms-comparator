package com.mdb.rdbms.comparator.controllers;

import com.mdb.rdbms.comparator.models.Order;
import com.mdb.rdbms.comparator.models.Store;
import com.mdb.rdbms.comparator.services.OrdersService;
import com.mdb.rdbms.comparator.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/stores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class StoreController {

    @Autowired
    StoreService service;



    @PostMapping
    public Store create(@RequestBody Store store){
        return service.create(store);
    }

    @GetMapping
    public List<Store> getStores(@RequestParam(name = "db", required = false, defaultValue="pg") String db){
        return this.service.getAllStores(db);
    }

    @GetMapping("search")
    public List<Store> searchStores(@RequestParam(name = "db", required = false, defaultValue="pg") String db, @RequestParam("searchTerm") String searchTerm){
        return this.service.searchStores(db, searchTerm);
    }

}
