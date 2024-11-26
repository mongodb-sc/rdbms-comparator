package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Order;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderJPARepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {



}
