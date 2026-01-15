package com.demo.bank_app.domain.model;

import com.demo.bank_app.domain.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LoanRequestStatusTransitionTest {

    @Test
    void updateStatus_FromPendingToApproved_ShouldSucceed() {
        // Arrange
        LoanRequest loanRequest = new LoanRequest("Demo User", 100.0, "EUR", "123456789R", new Date());

        // Act
        loanRequest.updateStatus(LoanRequestStatus.APPROVED);

        // Assert
        assertEquals(LoanRequestStatus.APPROVED, loanRequest.getStatus());
    }

    @Test
    void updateStatus_FromPendingToCancelled_ShouldFail() {
        // Arrange
        LoanRequest loanRequest = new LoanRequest("Demo User", 100.0, "EUR", "123456789R", new Date());

        // Act & Assert
        InvalidStateTransitionException exception = assertThrows(
                InvalidStateTransitionException.class,
                () -> loanRequest.updateStatus(LoanRequestStatus.CANCELLED)
        );

        assertTrue(exception.getMessage().contains("Invalid state transition from PENDING to CANCELLED"));
    }

}

