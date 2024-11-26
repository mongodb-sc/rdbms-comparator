package com.mdb.rdbms.comparator.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdb.rdbms.comparator.models.Customer;
import com.mdb.rdbms.comparator.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="api/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class CustomerController {

    @Autowired
    CustomerService service;




    @PostMapping
    public Customer create(@RequestBody Customer customer){
        return service.create(customer);
    }


    @PatchMapping("{id}")
    public Customer update(@PathVariable("id") Integer id, @RequestBody Customer customer){
        return service.update(id, customer);
    }

    @GetMapping
    public List<Customer> getCustomers(@RequestParam(name = "db", required = false, defaultValue="pg") String db,
                                       @RequestParam(name="firstName", required = false) String firstName,
                                       @RequestParam(name="lastName", required = false) String lastName,
                                       @RequestParam(name="title", required = false) String title,
                                       @RequestParam(name="city", required = false) String city,
                                       @RequestParam(name="state", required = false) String state,
                                       @RequestParam(name="street", required = false) String street,
                                       @RequestParam(name="zip", required = false) String zip,
                                       @RequestParam(name="phones", required = false) String phones,
                                       @RequestParam(name="emails", required = false) String emails){
        HashMap<String, String> phoneMap = null;
        HashMap<String, String> emailMap = null;

        try {
            if (phones != null) {
                phoneMap = new ObjectMapper().readValue(phones, HashMap.class);
            }
            if (emails != null) {
                emailMap = new ObjectMapper().readValue(emails, HashMap.class);
            }


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return this.service.getCustomers(db, firstName, lastName, title, city, state, street, zip, phoneMap, emailMap);
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Integer id,@RequestParam(name = "db", required = false, defaultValue="pg") String db ){
        return service.getCustomerById(id, db);
    }

}
