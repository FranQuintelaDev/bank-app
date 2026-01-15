package com.demo.bank_app.domain.port;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanRequestUseCase {
    LoanRequest save(LoanRequest loanRequest);
    LoanRequest findById(long id);
    LoanRequest findById(String id);
    Page<LoanRequest> findAll(Pageable pageable);
    LoanRequest updateStatus(long id, LoanRequestStatus status);
    LoanRequest updateStatus(String id, LoanRequestStatus status);
}
