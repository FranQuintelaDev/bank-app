package com.demo.bank_app.infrastructure.mapper;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.model.LoanRequestDto;
import com.demo.bank_app.model.PagedLoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagedResponseMapper {

    private final LoanRequestMapper loanRequestMapper;

    public PagedLoanResponse toPagedResponse(Page<LoanRequest> loanRequestPage) {
        Page<LoanRequestDto> dtoPage = loanRequestPage.map(loanRequestMapper::fromDomain);

        PagedLoanResponse response = new PagedLoanResponse();
        response.setContent(dtoPage.getContent());
        response.setPage(dtoPage.getNumber());
        response.setSize(dtoPage.getSize());
        response.setTotalElements(dtoPage.getTotalElements());
        response.setTotalPages(dtoPage.getTotalPages());
        response.setFirst(dtoPage.isFirst());
        response.setLast(dtoPage.isLast());

        return response;
    }
}

