package com.demo.bank_app.domain.model;
import com.demo.bank_app.domain.exception.InvalidStateTransitionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
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

    public LoanRequest() {
    }

    public LoanRequest(String applicantName, Double amount, String currency, String identificationNumber, Date applicationDate) {
        this.applicantName = applicantName;
        this.amount = amount;
        this.currency = currency;
        this.identificationNumber = identificationNumber;
        this.applicationDate = applicationDate;
        this.status = LoanRequestStatus.PENDING;
    }

    public LoanRequest(Long id, String applicantName, Double amount, String currency, String identificationNumber, Date applicationDate, LoanRequestStatus status) {
        this.id = id;
        this.applicantName = applicantName;
        this.amount = amount;
        this.currency = currency;
        this.identificationNumber = identificationNumber;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id;}

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LoanRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestStatus status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanRequest that = (LoanRequest) o;
        return Objects.equals(applicantName, that.applicantName)
                && Objects.equals(amount, that.amount)
                && Objects.equals(currency, that.currency)
                && Objects.equals(identificationNumber, that.identificationNumber)
                && Objects.equals(applicationDate, that.applicationDate)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantName, amount, currency, identificationNumber, applicationDate, status);
    }


}
