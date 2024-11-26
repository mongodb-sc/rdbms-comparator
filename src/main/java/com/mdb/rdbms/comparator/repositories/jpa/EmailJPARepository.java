package com.mdb.rdbms.comparator.repositories.jpa;

import com.mdb.rdbms.comparator.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJPARepository extends JpaRepository<Email, Integer> {
}
