package com.demo.bank_app.domain.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long id) {
        super("Loan request with ID " + id + " not found.");
    }

    public LoanNotFoundException(String message) {
        super(message);
    }
}
