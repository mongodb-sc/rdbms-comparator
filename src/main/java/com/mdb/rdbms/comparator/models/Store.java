package com.mdb.rdbms.comparator.models;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Entity(name="stores")
@Document("stores")
public class Store {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @MongoId
    Integer id;


    private String name;

    @OneToOne
    @JoinColumn(name="address_id")
    Address address;

    String managerName;
    String region;
    String storeType;
    BigDecimal sqFt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public BigDecimal getSqFt() {
        return sqFt;
    }

    public void setSqFt(BigDecimal sqFt) {
        this.sqFt = sqFt;
    }
}
