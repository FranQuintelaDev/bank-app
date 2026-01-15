package com.demo.bank_app.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_requests")
public class LoanRequestJpaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String identificationNumber;

    @Column(nullable = false)
    private String applicationDate;

    @Column(nullable = false)
    private String status;
}
