package com.mdb.rdbms.comparator.models;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name="addresses", indexes = {
    @Index(name = "city_idx", columnList = "city DESC")
})
public class Address{

    public Address(){}

    public Address(String city, String country, String state, String zip, String street){
        this.city = city;
        this.country = country;
        this.state = state;
        this.zip = zip;
        this.street = street;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String city;
    String state;
    String country;
    String zip;
    String street;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


}
