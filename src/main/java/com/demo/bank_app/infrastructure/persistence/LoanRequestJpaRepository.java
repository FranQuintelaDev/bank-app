package com.demo.bank_app.infrastructure.persistence;

import com.demo.bank_app.domain.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRequestJpaRepository extends JpaRepository<LoanRequest, Long> {
}
