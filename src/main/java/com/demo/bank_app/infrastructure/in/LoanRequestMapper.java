package com.demo.bank_app.infrastructure.in;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.model.LoanRequestDto;
import com.demo.bank_app.model.LoanStatusDto;
import org.springframework.stereotype.Component;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class LoanRequestMapper {

    public LoanRequest toDomain(LoanRequestDto dto) {
        LoanRequest domain = new LoanRequest();
        domain.setApplicantName(dto.getApplicantName());
        domain.setAmount(Double.valueOf(dto.getAmount()));
        domain.setCurrency(dto.getCurrency());
        domain.setApplicationDate( dto.getApplicationDate() == null ? null : Date.from( dto.getApplicationDate().toInstant() ) );
        domain.setIdentificationNumber(dto.getIdentificationNumber());
        domain.setStatus(dto.getStatus() != null ? toDomainStatus(dto.getStatus()) : LoanRequestStatus.PENDING);
        return domain;
    }
    public LoanRequestDto toDto(LoanRequest domain) {
        LoanRequestDto dto = new LoanRequestDto(
                domain.getApplicantName(),
                domain.getAmount().floatValue(),
                domain.getCurrency(),
                domain.getApplicationDate() == null ? null : domain.getApplicationDate().toInstant().atOffset(ZoneOffset.UTC),
                domain.getIdentificationNumber()
        );
        return dto;
    }
    public LoanRequestStatus toDomainStatus(LoanStatusDto statusEnum) {
        if (statusEnum == null) {
            return null;
        }
        return switch (statusEnum) {
            case APPROVED -> LoanRequestStatus.APPROVED;
            case REJECTED -> LoanRequestStatus.REJECTED;
            case PENDING -> LoanRequestStatus.PENDING;
            case CANCELED -> LoanRequestStatus.CANCELLED;
        };
    }
    public LoanStatusDto toDtoStatus(LoanRequestStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case APPROVED -> LoanStatusDto.APPROVED;
            case REJECTED -> LoanStatusDto.REJECTED;
            case PENDING -> LoanStatusDto.PENDING;
            case CANCELLED -> LoanStatusDto.CANCELED;
        };
    }
}
