package com.demo.bank_app.domain.exception;

import com.demo.bank_app.domain.model.LoanRequestStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(LoanRequestStatus currentStatus, LoanRequestStatus newStatus) {
        super(String.format("Invalid state transition from %s to %s", currentStatus, newStatus));
    }

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

