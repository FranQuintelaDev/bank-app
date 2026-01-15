package com.demo.bank_app.infrastructure.mapper;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.model.LoanRequestDto;
import com.demo.bank_app.model.LoanStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? toDomainStatus(dto.getStatus()) : com.demo.bank_app.domain.model.LoanRequestStatus.PENDING)")
    @Mapping(target = "applicationDate", source = "applicationDate", qualifiedByName = "offsetDateTimeToDate")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "floatToDouble")
    LoanRequest toDomain(LoanRequestDto dto);

    @Mapping(target = "id", source = "id", qualifiedByName = "longToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "domainStatusToDto")
    @Mapping(target = "applicationDate", source = "applicationDate", qualifiedByName = "dateToOffsetDateTime")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "doubleToFloat")
    LoanRequestDto fromDomain(LoanRequest domain);

    @Named("offsetDateTimeToDate")
    default Date offsetDateTimeToDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
    }

    @Named("dateToOffsetDateTime")
    default OffsetDateTime dateToOffsetDateTime(Date date) {
        return date == null ? null : date.toInstant().atOffset(ZoneOffset.UTC);
    }

    @Named("floatToDouble")
    default Double floatToDouble(Float value) {
        return value == null ? null : value.doubleValue();
    }

    @Named("doubleToFloat")
    default Float doubleToFloat(Double value) {
        return value == null ? null : value.floatValue();
    }

    @Named("longToString")
    default String longToString(Long value) {
        return value == null ? null : value.toString();
    }

    @Named("domainStatusToDto")
    default LoanStatusDto fromDomainStatus(LoanRequestStatus status) {
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

    default LoanRequestStatus toDomainStatus(LoanStatusDto statusEnum) {
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
}
