package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneJPARepository extends JpaRepository<Phone, Integer> {
}
