package com.demo.bank_app.infrastructure.out.persistence;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.domain.port.out.LoanRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestRepositoryAdapter implements LoanRequestRepository {

    private final LoanRequestJpaRepository jpaRepository;

    public LoanRequestRepositoryAdapter(LoanRequestJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LoanRequest save(LoanRequest loanRequest) {
        return jpaRepository.save(loanRequest);
    }

    @Override
    public LoanRequest findById(long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public Page<LoanRequest> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public LoanRequest updateStatus(long id, LoanRequestStatus status) {
        LoanRequest loanRequest = findById(id);
        if (loanRequest != null) {
            loanRequest.setStatus(status);
            return jpaRepository.save(loanRequest);
        }
        return null;
    }
}

