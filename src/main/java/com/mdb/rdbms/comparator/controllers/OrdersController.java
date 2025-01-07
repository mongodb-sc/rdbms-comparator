package com.mdb.rdbms.comparator.controllers;

import com.mdb.rdbms.comparator.models.Order;
import com.mdb.rdbms.comparator.models.OrderSearch;
import com.mdb.rdbms.comparator.models.Response;
import com.mdb.rdbms.comparator.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class OrdersController {

    @Autowired
    OrdersService service;



    @PostMapping
    public Order create(@RequestBody Order order){
        return service.create(order);
    }

    @PostMapping("batch")
    public List<Order> create(@RequestBody List<Order> orders){
        return service.createBatch(orders);

    }


    @GetMapping
    public Page<Order> getAllOrders(@RequestParam(name = "db", required = false, defaultValue="pg") String db, @RequestParam(name="page", required = false, defaultValue = "0") int page){
        return this.service.getAllOrders(db, null, page);
    }

    @GetMapping("recent")
    public Response<List<Order>> getRecentOrders(@RequestParam(name = "db", required = false, defaultValue="pg") String db, @RequestParam(name="customerId") int customerId){
        return this.service.getRecentOrders(db, customerId);
    }


    @PostMapping("search")
    public Page<Order> getOrders(@RequestParam(name = "db", required = false, defaultValue="pg") String db, @RequestBody OrderSearch orderSearch, @RequestParam(name="page", required = false, defaultValue = "0") int page){
        return this.service.getAllOrders(db, orderSearch, page);
    }

}
