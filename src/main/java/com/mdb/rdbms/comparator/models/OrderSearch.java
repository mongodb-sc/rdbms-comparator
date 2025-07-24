package com.mdb.rdbms.comparator.models;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSearch {


    Integer id;
    Date orderDate;
    Integer warehouseId;
    Date fillDate;
    String purchaseOrder;
    Integer invoiceId;
    Date invoiceDate;
    String deliveryMethod;
    BigDecimal weight;
    Integer totalPieces;
    Date pickDate;
    String shippingMethod;
    Integer billingDept;
    String orderStatus;
    String shippingStatus;
    Date deliveryDate;
    Integer orderType;
    Integer employeeId;
    Address shippingAddress;
    Customer customer;
    Store store;
    BigDecimal total;
    boolean lucene;
    String location = "US";


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Date getFillDate() {
        return fillDate;
    }

    public void setFillDate(Date fillDate) {
        this.fillDate = fillDate;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Integer totalPieces) {
        this.totalPieces = totalPieces;
    }

    public Date getPickDate() {
        return pickDate;
    }

    public void setPickDate(Date pickDate) {
        this.pickDate = pickDate;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Integer getBillingDept() {
        return billingDept;
    }

    public void setBillingDept(Integer billingDept) {
        this.billingDept = billingDept;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isLucene() {
        return lucene;
    }

    public void setLucene(boolean lucene) {
        this.lucene = lucene;
    }

    public String getLocation() {
        return location;
    }


    @Override
    public String toString() {
        return "OrderSearch{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", warehouseId=" + warehouseId +
                ", fillDate=" + fillDate +
                ", purchaseOrder='" + purchaseOrder + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", weight=" + weight +
                ", totalPieces=" + totalPieces +
                ", pickDate=" + pickDate +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", billingDept=" + billingDept +
                ", orderStatus='" + orderStatus + '\'' +
                ", shippingStatus='" + shippingStatus + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", orderType=" + orderType +
                ", employeeId=" + employeeId +
                ", shippingAddress=" + shippingAddress +
                ", customer=" + customer +
                ", store=" + store +
                ", total=" + total +
                ", lucene=" + lucene +
                '}';
    }
}
