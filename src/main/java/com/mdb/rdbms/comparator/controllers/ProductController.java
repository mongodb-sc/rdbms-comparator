package com.mdb.rdbms.comparator.controllers;


import com.mdb.rdbms.comparator.models.Product;
import com.mdb.rdbms.comparator.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class ProductController {

    @Autowired
    ProductService service;


    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }


    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false, defaultValue = "pg") String db) {
        return service.getAllProducts(db);
    }

    @GetMapping("search")
    public List<Product> searchProducts(@RequestParam(name = "db", required=false, defaultValue="pg") String db, @RequestParam("searchTerm") String searchTerm) {
        return service.searchProducts(db, searchTerm);
    }
}
