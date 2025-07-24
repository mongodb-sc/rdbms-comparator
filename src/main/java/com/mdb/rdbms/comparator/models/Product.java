package com.mdb.rdbms.comparator.models;


import jakarta.persistence.*;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Entity
@Table(name="products")
@Document("products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Transient
    @MongoId
    String _id;

    String name;
    int dept;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal price;
    String description;
    Integer pg_id;

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

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getPg_id() {
        return pg_id;
    }

    public void setPg_id(Integer pg_id) {
        this.pg_id = pg_id;
    }
}
