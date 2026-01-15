package com.demo.bank_app.application.service;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.domain.port.in.LoanRequestUseCase;
import com.demo.bank_app.domain.port.out.LoanRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoanRequestService implements LoanRequestUseCase {
    private final LoanRequestRepository loanRequestRepository;

    public LoanRequestService(LoanRequestRepository loanRequestRepository) {
        this.loanRequestRepository = loanRequestRepository;
    }

    @Override
    public LoanRequest save(LoanRequest loanRequest) {
        return this.loanRequestRepository.save(loanRequest);
    }

    @Override
    public LoanRequest findById(long id) {
        this.loanRequestRepository.findById(id);
        return this.loanRequestRepository.findById(id);
    }

    @Override
    public Page<LoanRequest> findAll(Pageable pageable) {
        return loanRequestRepository.findAll(pageable);
    }

    @Override
    public LoanRequest updateStatus(long id, LoanRequestStatus status) {
        var loanRequest = this.loanRequestRepository.findById(id);
        if (loanRequest == null) {
            throw new IllegalArgumentException("LoanRequest not found with id: " + id);
        }
        loanRequest.updateStatus(status);
        return this.loanRequestRepository.save(loanRequest);
    }
}
