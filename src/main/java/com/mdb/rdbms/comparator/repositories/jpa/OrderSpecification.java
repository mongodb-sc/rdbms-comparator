package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Address;
import com.mdb.rdbms.comparator.models.Customer;
import com.mdb.rdbms.comparator.models.Order;
import com.mdb.rdbms.comparator.models.OrderSearch;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {

    private OrderSearch orderSearch;

    public OrderSpecification(OrderSearch orderSearch) {
        this.orderSearch = orderSearch;
    }


    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (orderSearch.getOrderDate() != null) {
            predicates.add(cb.equal(root.get("orderDate"), orderSearch.getOrderDate()));
        }
        if (orderSearch.getId() != null) {
            System.out.println("Looklup by ID got called");
            predicates.add(cb.equal(root.get("id"), orderSearch.getId()));
        }
        if (!orderSearch.getPurchaseOrder().isEmpty()) {
            predicates.add(cb.equal(root.get("purchaseOrder"), orderSearch.getPurchaseOrder()));
        }
        if (orderSearch.getInvoiceDate() != null) {
            predicates.add(cb.equal(root.get("invoiceDate"), orderSearch.getInvoiceDate()));
        }
        if (orderSearch.getInvoiceId() != null) {
            predicates.add(cb.equal(root.get("invoiceId"), orderSearch.getInvoiceId()));
        }
        if (orderSearch.getFillDate() != null) {
            predicates.add(cb.equal(root.get("fillDate"), orderSearch.getFillDate()));
        }
        if (!orderSearch.getDeliveryMethod().isEmpty()) {
            predicates.add(cb.equal(root.get("deliveryMethod"), orderSearch.getDeliveryMethod()));
        }
        if (orderSearch.getWeight() != null) {
            predicates.add(cb.equal(root.get("weight"), orderSearch.getWeight()));
        }
        if (orderSearch.getTotalPieces() != null) {
            predicates.add(cb.equal(root.get("totalPieces"), orderSearch.getTotalPieces()));
        }
        if (orderSearch.getPickDate() != null) {
            predicates.add(cb.equal(root.get("pickDate"), orderSearch.getPickDate()));
        }
        if (!orderSearch.getShippingMethod().isEmpty()) {
            predicates.add(cb.equal(root.get("shippingMethod"), orderSearch.getShippingMethod()));
        }
        if (orderSearch.getBillingDept() != null) {
            predicates.add(cb.equal(root.get("billingDept"), orderSearch.getBillingDept()));
        }
        if (!orderSearch.getOrderStatus().isEmpty()) {
            predicates.add(cb.equal(root.get("orderStatus"), orderSearch.getOrderStatus()));
        }
        if (!orderSearch.getShippingStatus().isEmpty()) {
            predicates.add(cb.equal(root.get("shippingStatus"), orderSearch.getShippingStatus()));
        }
        if (orderSearch.getDeliveryDate() != null) {
            predicates.add(cb.equal(root.get("deliveryDate"), orderSearch.getDeliveryDate()));
        }
        if (orderSearch.getOrderType() != null) {
            predicates.add(cb.equal(root.get("orderType"), orderSearch.getOrderType()));
        }
        if (orderSearch.getEmployeeId() != null) {
            predicates.add(cb.equal(root.get("employeeId"), orderSearch.getEmployeeId()));
        }
        if (orderSearch.getTotal() != null) {
            predicates.add(cb.equal(root.get("total"), orderSearch.getTotal()));
        }
        if (orderSearch.getWarehouseId() != null) {
            predicates.add(cb.equal(root.get("warehouseId"), orderSearch.getWarehouseId()));
        }
        if (orderSearch.getShippingAddress() != null) {
            Join<Order, Address> addressJoin = root.join("shippingAddress");
            if (!orderSearch.getShippingAddress().getCity().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("city"), orderSearch.getShippingAddress().getCity()));
            }
            if (!orderSearch.getShippingAddress().getState().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("state"), orderSearch.getShippingAddress().getState()));
            }
            if (!orderSearch.getShippingAddress().getStreet().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("street"), orderSearch.getShippingAddress().getStreet()));
            }
            if (!orderSearch.getShippingAddress().getZip().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("zip"), orderSearch.getShippingAddress().getZip()));
            }
        }
        if (orderSearch.getCustomer() != null) {
            Join<Order, Customer> customerJoin = root.join("customer");
            if (!orderSearch.getCustomer().getFirstName().isEmpty()) {
                predicates.add(cb.equal(customerJoin.get("firstName"), orderSearch.getCustomer().getFirstName()));
            }
            if (!orderSearch.getCustomer().getLastName().isEmpty()) {
                predicates.add(cb.equal(customerJoin.get("lastName"), orderSearch.getCustomer().getLastName()));
            }
            if (!orderSearch.getCustomer().getTitle().isEmpty()) {
                predicates.add(cb.equal(customerJoin.get("title"), orderSearch.getCustomer().getTitle()));
            }
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }


}
