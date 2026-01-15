package com.demo.bank_app.domain.model;

import com.demo.bank_app.domain.exception.InvalidStateTransitionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_request")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Applicant name is required")
    private String applicantName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "1000000.00", message = "Amount cannot exceed 1,000,000")
    private Double amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    @NotNull(message = "Application date is required")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private LoanRequestStatus status;

    public LoanRequest(String applicantName, Double amount, String currency, String identificationNumber, Date applicationDate) {
        this.applicantName = applicantName;
        this.amount = amount;
        this.currency = currency;
        this.identificationNumber = identificationNumber;
        this.applicationDate = applicationDate;
        this.status = LoanRequestStatus.PENDING;
    }

    public void updateStatus(LoanRequestStatus newStatus) {
        if (!isValidTransition(this.status, newStatus)) {
            throw new InvalidStateTransitionException(this.status, newStatus);
        }
        this.status = newStatus;
    }

    private boolean isValidTransition(LoanRequestStatus currentStatus, LoanRequestStatus newStatus) {
        return switch (currentStatus) {
            case PENDING -> newStatus == LoanRequestStatus.APPROVED || newStatus == LoanRequestStatus.REJECTED;
            case APPROVED -> newStatus == LoanRequestStatus.CANCELLED;
            case REJECTED, CANCELLED -> false;
        };
    }
}
