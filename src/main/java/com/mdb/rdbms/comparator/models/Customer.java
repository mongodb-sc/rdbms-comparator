package com.mdb.rdbms.comparator.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name = "customers", indexes = {
    @Index(name = "first_last_idx", columnList = "firstName, lastName DESC")
})
@BatchSize(size=50)
@Document("customers")
public class Customer {

    public Customer(){};

    public Customer(String firstName, String lastName, String title, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.address = address;
    }

    String location = "US";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Transient
    @MongoId
    String _id;

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

    @JsonIgnoreProperties({"customer"})
    @Transient
    private List<Order> recentOrders;

    @Transient
    private Meta meta;

    Integer pg_id;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Order> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<Order> recentOrders) {
        this.recentOrders = recentOrders;
    }

    public Integer getPg_id() {
        return this.pg_id;
    }

    public void setPg_id(Integer pg_id) {
        this.pg_id = pg_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
