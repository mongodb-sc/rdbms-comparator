package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJPARepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameStartingWith(String searchTerm);
}
