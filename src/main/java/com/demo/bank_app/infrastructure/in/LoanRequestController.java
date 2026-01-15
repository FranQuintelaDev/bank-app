package com.demo.bank_app.infrastructure.in;

import com.demo.bank_app.api.LoansApi;
import com.demo.bank_app.domain.exception.InvalidLoanRequestException;
import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.port.in.LoanRequestUseCase;
import com.demo.bank_app.model.LoanRequestDto;
import com.demo.bank_app.model.PagedLoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanRequestController implements LoansApi {
    private final LoanRequestUseCase loanRequestUseCase;
    private final LoanRequestMapper loanRequestMapper;

    public LoanRequestController(LoanRequestUseCase loanRequestUseCase, LoanRequestMapper loanRequestMapper) {
        this.loanRequestUseCase = loanRequestUseCase;
        this.loanRequestMapper = loanRequestMapper;
    }

    @Override
    public ResponseEntity<PagedLoanResponse> loansGet(
            Integer page,
            Integer size,
            String sort,
            String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<LoanRequest> loanRequests = loanRequestUseCase.findAll(pageable);
        Page<LoanRequestDto> dtoPage = loanRequests.map(loanRequestMapper::toDto);

        PagedLoanResponse pagedResponse = new PagedLoanResponse();
        pagedResponse.setContent(dtoPage.getContent());
        pagedResponse.setPage(dtoPage.getNumber());
        pagedResponse.setSize(dtoPage.getSize());
        pagedResponse.setTotalElements(dtoPage.getTotalElements());
        pagedResponse.setTotalPages(dtoPage.getTotalPages());
        pagedResponse.setFirst(dtoPage.isFirst());
        pagedResponse.setLast(dtoPage.isLast());

        return ResponseEntity.ok(pagedResponse);
    }


    @Override
    public ResponseEntity<LoanRequestDto> loansPost(LoanRequestDto loanRequestDto) {
        var domainRequest = loanRequestMapper.toDomain(loanRequestDto);
        var savedRequest = loanRequestUseCase.save(domainRequest);
        var responseDto = loanRequestMapper.toDto(savedRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }
    @Override
    public ResponseEntity<LoanRequestDto> loansIdGet(String id) {
        try {
            var domainRequest = loanRequestUseCase.findById(Long.parseLong(id));
            var responseDto = loanRequestMapper.toDto(domainRequest);
            return ResponseEntity.ok(responseDto);
        } catch (NumberFormatException e) {
            throw new InvalidLoanRequestException("Invalid loan request ID format: " + id);
        }
    }
    @Override
    public ResponseEntity<LoanRequestDto> loansIdPatch(String id, LoanRequestDto loanRequestDto) {
        var domainStatus = loanRequestMapper.toDomainStatus(loanRequestDto.getStatus());
        var updatedRequest = loanRequestUseCase.updateStatus(Long.parseLong(id), domainStatus);
        var responseDto = loanRequestMapper.toDto(updatedRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}
