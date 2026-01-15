package com.demo.bank_app.application.useCase;

import com.demo.bank_app.domain.exception.InvalidLoanRequestException;
import com.demo.bank_app.domain.exception.LoanNotFoundException;
import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.domain.port.LoanRequestUseCase;
import com.demo.bank_app.domain.port.LoanRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanRequestUseCaseImpl implements LoanRequestUseCase {

    private final LoanRequestRepository loanRequestRepository;

    @Override
    public LoanRequest save(LoanRequest loanRequest) {
        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public LoanRequest findById(long id) {
        LoanRequest loanRequest = loanRequestRepository.findById(id);
        if (loanRequest == null) {
            throw new LoanNotFoundException("Loan request not found with id: " + id);
        }
        return loanRequest;
    }

    @Override
    public LoanRequest findById(String id) {
        try {
            return findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new InvalidLoanRequestException("Invalid loan request ID format: " + id);
        }
    }

    @Override
    public Page<LoanRequest> findAll(Pageable pageable) {
        return loanRequestRepository.findAll(pageable);
    }

    @Override
    public LoanRequest updateStatus(long id, LoanRequestStatus status) {
        LoanRequest loanRequest = loanRequestRepository.findById(id);
        if (loanRequest == null) {
            throw new LoanNotFoundException("Loan request not found with id: " + id);
        }
        loanRequest.updateStatus(status);
        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public LoanRequest updateStatus(String id, LoanRequestStatus status) {
        try {
            return updateStatus(Long.parseLong(id), status);
        } catch (NumberFormatException e) {
            throw new InvalidLoanRequestException("Invalid loan request ID format: " + id);
        }
    }
}
