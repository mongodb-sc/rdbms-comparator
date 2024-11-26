package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJPARepository extends JpaRepository<Store, Integer> {
}
