package com.demo.bank_app.infrastructure.adapter.in;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.infrastructure.mapper.LoanRequestMapper;
import com.demo.bank_app.model.LoanRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanRequestMapperTest {

    private LoanRequestMapper loanRequestMapper;

    private LoanRequest loanRequest;
    private LoanRequestDto loanRequestDto;

    @BeforeEach
    public void setup() {
        loanRequestMapper = Mappers.getMapper(LoanRequestMapper.class);

        Date applicationDate = new Date();
        OffsetDateTime dateInUtc = applicationDate.toInstant().atOffset(ZoneOffset.UTC);

        loanRequest = new LoanRequest(
                "Demo User",
                100.0,
                "EUR",
                "123456789R",
                applicationDate);
        loanRequest.setId(1L);

        loanRequestDto = new LoanRequestDto(
                "Demo User",
                100.0f,
                "EUR",
                dateInUtc,
                "123456789R");
        loanRequestDto.setId("1");
    }

    @Test
    public void toDomain_ShouldMapDtoToDomain() {
        // Act
        LoanRequest loanRequestTest = loanRequestMapper.toDomain(loanRequestDto);

        // Assert
        assertEquals(loanRequest.getApplicantName(), loanRequestTest.getApplicantName());
        assertEquals(loanRequest.getAmount(), loanRequestTest.getAmount());
        assertEquals(loanRequest.getCurrency(), loanRequestTest.getCurrency());
        assertEquals(loanRequest.getIdentificationNumber(), loanRequestTest.getIdentificationNumber());
        assertEquals(loanRequest.getApplicationDate().toInstant().toEpochMilli(),
                loanRequestTest.getApplicationDate().toInstant().toEpochMilli());
    }

    @Test
    public void fromDomain_ShouldMapDomainToDto() {
        // Act
        LoanRequestDto loanRequestDtoTest = loanRequestMapper.fromDomain(loanRequest);

        // Assert
        assertEquals(loanRequestDto.getApplicantName(), loanRequestDtoTest.getApplicantName());
        assertEquals(loanRequestDto.getAmount(), loanRequestDtoTest.getAmount());
        assertEquals(loanRequestDto.getCurrency(), loanRequestDtoTest.getCurrency());
        assertEquals(loanRequestDto.getIdentificationNumber(), loanRequestDtoTest.getIdentificationNumber());
        assertEquals(loanRequestDto.getApplicationDate().toInstant().toEpochMilli(),
                loanRequestDtoTest.getApplicationDate().toInstant().toEpochMilli());
    }
}
