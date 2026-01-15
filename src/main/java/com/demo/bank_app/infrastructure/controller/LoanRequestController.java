package com.demo.bank_app.infrastructure.controller;

import com.demo.bank_app.api.LoansApi;
import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.port.LoanRequestUseCase;
import com.demo.bank_app.infrastructure.mapper.LoanRequestMapper;
import com.demo.bank_app.infrastructure.mapper.PagedResponseMapper;
import com.demo.bank_app.model.LoanRequestDto;
import com.demo.bank_app.model.PagedLoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoanRequestController implements LoansApi {

    private final LoanRequestUseCase loanRequestUseCase;
    private final LoanRequestMapper loanRequestMapper;
    private final PagedResponseMapper pagedResponseMapper;

    @Override
    public ResponseEntity<PagedLoanResponse> getLoans(
            Integer page,
            Integer size,
            String sort,
            String direction
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;
        String sortField = sort != null ? sort : "id";
        String sortDirection = direction != null ? direction : "ASC";

        Sort.Direction sortDir = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDir, sortField));

        Page<LoanRequest> loanRequests = loanRequestUseCase.findAll(pageable);
        PagedLoanResponse pagedResponse = pagedResponseMapper.toPagedResponse(loanRequests);

        return ResponseEntity.ok(pagedResponse);
    }

    @Override
    public ResponseEntity<LoanRequestDto> createLoan(LoanRequestDto loanRequestDto) {
        LoanRequest domainRequest = loanRequestMapper.toDomain(loanRequestDto);
        LoanRequest savedRequest = loanRequestUseCase.save(domainRequest);
        LoanRequestDto responseDto = loanRequestMapper.fromDomain(savedRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Override
    public ResponseEntity<LoanRequestDto> getLoanById(String id) {
        LoanRequest domainRequest = loanRequestUseCase.findById(id);
        LoanRequestDto responseDto = loanRequestMapper.fromDomain(domainRequest);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<LoanRequestDto> updateLoan(String id, LoanRequestDto loanRequestDto) {
        var domainStatus = loanRequestMapper.toDomainStatus(loanRequestDto.getStatus());
        LoanRequest updatedRequest = loanRequestUseCase.updateStatus(id, domainStatus);
        LoanRequestDto responseDto = loanRequestMapper.fromDomain(updatedRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
