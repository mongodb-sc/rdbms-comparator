package com.mdb.rdbms.comparator.controllers;

import com.mdb.rdbms.comparator.models.Customer;
import com.mdb.rdbms.comparator.models.CustomerSearch;
import com.mdb.rdbms.comparator.models.MetricsPage;
import com.mdb.rdbms.comparator.models.Response;
import com.mdb.rdbms.comparator.services.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins= "http://localhost:4200")
public class CustomerController {

    Logger logger = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerService service;




    @PostMapping
    public Response<Customer> create(@RequestBody Customer customer){
        return service.create(customer);
    }


    @PatchMapping("{id}")
    public Customer update(@PathVariable("id") Integer id, @RequestBody Customer customer){
        return service.update(id, customer);
    }

    @PostMapping("search")
    public Page<Customer> getCustomers(@RequestParam(name = "db", required = false, defaultValue="pg") String db,
                                       @RequestBody CustomerSearch customerSearch,
                                       @RequestParam(name="page", required = false, defaultValue = "0") int page){


        return this.service.getCustomers(db, customerSearch, page);
    }

    @GetMapping("search")
    public MetricsPage<Customer> searchCustomers(@RequestParam("searchTerm") String searchTerm, @RequestParam(name="page", required = false, defaultValue = "0") int page) {
        return this.service.searchCustomersSimple(searchTerm, page);
    }

    @GetMapping("autocomplete")
    public List<Customer> autocompleteSearch(@RequestParam("searchTerm") String searchTerm) {
        return this.service.searchCustomersSimple(searchTerm);
    }


    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Integer id,@RequestParam(name = "db", required = false, defaultValue="pg") String db ){
        return service.getCustomerById(id, db);
    }

}
