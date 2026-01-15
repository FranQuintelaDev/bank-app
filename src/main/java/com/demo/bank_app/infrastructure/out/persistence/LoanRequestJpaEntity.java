package com.demo.bank_app.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "loan_requests")
public class LoanRequestJpaEntity {
    @Id
    private Long id;

    @Setter
    @Column(nullable = false)
    private String applicantName;

    @Setter
    @Getter
    @Column(nullable = false)
    private double amount;

    @Setter
    @Getter
    @Column(nullable = false)
    private String currency;

    @Setter
    @Getter
    @Column(nullable = false)
    private String identificationNumber;

    @Setter
    @Getter
    @Column(nullable = false)
    private String applicationDate;

    @Getter
    @Setter
    @Column(nullable = false)
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanRequestJpaEntity that = (LoanRequestJpaEntity) o;
        return Double.compare(amount, that.amount) == 0
                && Objects.equals(applicantName, that.applicantName)
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
