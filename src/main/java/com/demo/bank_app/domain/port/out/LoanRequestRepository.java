package com.demo.bank_app.domain.port.out;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanRequestRepository {
    LoanRequest save(LoanRequest loanRequest);
    LoanRequest findById(long id);
    Page<LoanRequest> findAll(Pageable pageable);
    LoanRequest updateStatus(long id, LoanRequestStatus status);
}
