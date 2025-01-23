package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Product;
import com.mdb.rdbms.comparator.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreJPARepository extends JpaRepository<Store, Integer> {

    List<Store> findByNameStartingWith(String searchTerm);
}
