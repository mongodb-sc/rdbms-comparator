package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Order;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderJPARepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {


    @Query("Select o from Order o where o.customer.id = :customerId order by o.orderDate desc, o.orderStatus asc limit 5")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId);

}
