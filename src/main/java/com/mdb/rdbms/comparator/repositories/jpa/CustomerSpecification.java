package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.*;
import com.mdb.rdbms.comparator.models.Order;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification implements Specification<Customer> {

    CustomerSearch customerSearch;
    boolean fuzzySearch = false;

    public CustomerSpecification(CustomerSearch customerSearch) {
        this.customerSearch = customerSearch;
        this.fuzzySearch = customerSearch.isFuzzySearch();
    }


    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (!customerSearch.getFirstName().isEmpty()) {
            if (fuzzySearch){
                predicates.add(cb.like(root.get("firstName"), customerSearch.getFirstName()));
            } else {
                predicates.add(cb.equal(root.get("firstName"), customerSearch.getFirstName()));
            }

        }
        if (!customerSearch.getLastName().isEmpty()) {
            if (fuzzySearch){
                predicates.add(cb.like(root.get("lastName"), customerSearch.getLastName()));
            } else {
                predicates.add(cb.equal(root.get("lastName"), customerSearch.getLastName()));
            }

        }
        if (!customerSearch.getTitle().isEmpty()) {
            if (fuzzySearch){
                predicates.add(cb.like(root.get("title"), customerSearch.getTitle()));
            } else {
                predicates.add(cb.equal(root.get("title"), customerSearch.getTitle()));
            }
        }
        if (!customerSearch.getCity().isEmpty() || !customerSearch.getState().isEmpty() || !customerSearch.getStreet().isEmpty() || !customerSearch.getZip().isEmpty()) {
            Join<Customer, Address> addressJoin = root.join("address");
            if (!customerSearch.getCity().isEmpty()) {
                if (fuzzySearch){
                    predicates.add(cb.like(addressJoin.get("city"), customerSearch.getCity()));
                } else {
                    predicates.add(cb.equal(addressJoin.get("city"), customerSearch.getCity()));
                }
            }
            if (!customerSearch.getState().isEmpty()) {
                if (fuzzySearch){
                    predicates.add(cb.like(addressJoin.get("state"), customerSearch.getState()));
                } else {
                    predicates.add(cb.equal(addressJoin.get("state"), customerSearch.getState()));
                }
            }
            if (!customerSearch.getStreet().isEmpty()) {
                if (fuzzySearch) {
                    predicates.add(cb.like(addressJoin.get("street"), customerSearch.getStreet()));
                } else {
                    predicates.add(cb.equal(addressJoin.get("street"), customerSearch.getStreet()));
                }
            }
            if (!customerSearch.getZip().isEmpty()) {
                if (fuzzySearch){
                    predicates.add(cb.like(addressJoin.get("zip"), customerSearch.getZip()));
                } else {
                    predicates.add(cb.equal(addressJoin.get("zip"), customerSearch.getZip()));
                }
            }
        }
        if (customerSearch.getPhone() != null && !customerSearch.getPhone().getNumber().isEmpty()) {
            Join<Customer, Phone> phoneJoin = root.join("phones");
            if (fuzzySearch) {
                predicates.add(cb.like(phoneJoin.get("phone"), customerSearch.getPhone().getNumber()));
            } else {
                predicates.add(cb.equal(phoneJoin.get("type"), customerSearch.getPhone().getType()));
                predicates.add(cb.equal(phoneJoin.get("number"), customerSearch.getPhone().getNumber()));
            }
        }
        if (customerSearch.getEmail() != null && !customerSearch.getEmail().getEmail().isEmpty()) {
            Join<Customer, Email> emailJoin = root.join("emails");
            if (fuzzySearch) {
                predicates.add(cb.like(emailJoin.get("email"), customerSearch.getEmail().getEmail()));
            } else {
                predicates.add(cb.equal(emailJoin.get("type"), customerSearch.getEmail().getType()));
                predicates.add(cb.equal(emailJoin.get("email"), customerSearch.getEmail().getEmail()));
            }
        }


        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
