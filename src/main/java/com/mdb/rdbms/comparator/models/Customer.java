package com.mdb.rdbms.comparator.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigInteger;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name = "customers", indexes = {
    @Index(name = "first_last_idx", columnList = "firstName, lastName DESC")
})
@Document("customers")
@CompoundIndex(name = "first_last_city_idx", def = "{'firstname': 1, 'lastname': 1, 'address.city': 1}")
public class Customer {

    public Customer(){};

    public Customer(String firstName, String lastName, String title, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.address = address;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    String firstName;
    String lastName;
    String title;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id", referencedColumnName="id")
    List<Phone> phones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id", referencedColumnName="id")
    List<Email> emails = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="address_id")
    Address address;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
