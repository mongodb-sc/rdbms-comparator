package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.*;
import com.mdb.rdbms.comparator.models.Order;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification implements Specification<Customer> {

    CustomerSearch customerSearch;

    public CustomerSpecification(CustomerSearch customerSearch) {
        this.customerSearch = customerSearch;
    }


    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (!customerSearch.getFirstName().isEmpty()) {
            predicates.add(cb.equal(root.get("firstName"), customerSearch.getFirstName()));
        }
        if (!customerSearch.getLastName().isEmpty()) {
            predicates.add(cb.equal(root.get("lastName"), customerSearch.getLastName()));
        }
        if (!customerSearch.getTitle().isEmpty()) {
            predicates.add(cb.equal(root.get("title"), customerSearch.getTitle()));
        }
        if (!customerSearch.getCity().isEmpty() || !customerSearch.getState().isEmpty() || !customerSearch.getStreet().isEmpty() || !customerSearch.getZip().isEmpty()) {
            Join<Customer, Address> addressJoin = root.join("address");
            if (!customerSearch.getCity().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("city"), customerSearch.getCity()));
            }
            if (!customerSearch.getState().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("state"), customerSearch.getState()));
            }
            if (!customerSearch.getStreet().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("street"), customerSearch.getStreet()));
            }
            if (!customerSearch.getZip().isEmpty()) {
                predicates.add(cb.equal(addressJoin.get("zip"), customerSearch.getZip()));
            }
        }
        if (customerSearch.getPhone() != null && !customerSearch.getPhone().getNumber().isEmpty()) {
            Join<Customer, Phone> phoneJoin = root.join("phones");
            predicates.add(cb.equal(phoneJoin.get("type"), customerSearch.getPhone().getType()));
            predicates.add(cb.equal(phoneJoin.get("number"), customerSearch.getPhone().getNumber()));
        }
        if (customerSearch.getEmail() != null && !customerSearch.getEmail().getEmail().isEmpty()) {
            Join<Customer, Email> emailJoin = root.join("emails");
            predicates.add(cb.equal(emailJoin.get("type"), customerSearch.getEmail().getType()));
            predicates.add(cb.equal(emailJoin.get("email"), customerSearch.getEmail().getEmail()));
        }


        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
