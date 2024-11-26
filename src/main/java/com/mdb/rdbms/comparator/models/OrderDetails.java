package com.mdb.rdbms.comparator.models;


import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Entity
@Table(name="order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    Product product;

    @Transient
    Integer product_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
}
