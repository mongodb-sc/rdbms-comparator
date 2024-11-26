package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressJpaRepository extends JpaRepository<Address, Integer> {
}
