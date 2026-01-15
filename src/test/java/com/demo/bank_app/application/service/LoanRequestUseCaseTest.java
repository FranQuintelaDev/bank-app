package com.demo.bank_app.application.service;

import com.demo.bank_app.domain.model.LoanRequest;
import com.demo.bank_app.domain.model.LoanRequestStatus;
import com.demo.bank_app.domain.port.out.LoanRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanRequestUseCaseTest {

    @Mock
    private LoanRequestRepository loanRequestRepository;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private LoanRequest loanRequest;

    @BeforeEach
    public void setUp() {
        loanRequest = new LoanRequest(
                "Demo User",
                100.0,
                "EUR",
                "123456789R",
                new Date());
    }

    @Test
    void save_ShouldReturnSavedLoanRequest_WhenValidInput() {
        // Arrange
        when(loanRequestRepository.save(any(LoanRequest.class))).thenReturn(loanRequest);

        // Act
        LoanRequest savedLoanRequest = loanRequestService.save(loanRequest);

        // Assert
        assertEquals(loanRequest, savedLoanRequest);
        assertEquals(LoanRequestStatus.PENDING, savedLoanRequest.getStatus());
        verify(loanRequestRepository, times(1)).save(loanRequest);
    }

    @Test
    void findById_ShouldReturnLoanRequest_WhenExists() {
        // Arrange
        when(loanRequestRepository.findById(1L)).thenReturn(loanRequest);

        // Act
        LoanRequest foundLoanRequest = loanRequestService.findById(1L);

        // Assert
        assertNotNull(foundLoanRequest);
        assertEquals(loanRequest, foundLoanRequest);
        verify(loanRequestRepository, times(2)).findById(1L);
    }

    @Test
    void findAll_ShouldReturnPageOfLoanRequests() {
        // Arrange
        List<LoanRequest> loanRequests = List.of(loanRequest);
        Page<LoanRequest> page = new PageImpl<>(loanRequests);
        Pageable pageable = PageRequest.of(0, 10);

        when(loanRequestRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<LoanRequest> result = loanRequestService.findAll(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(loanRequest, result.getContent().get(0));
        verify(loanRequestRepository, times(1)).findAll(pageable);
    }

    @Test
    void updateStatus_ShouldUpdateStatus_WhenLoanRequestExists() {
        // Arrange
        loanRequest.setId(1L);
        when(loanRequestRepository.findById(1L)).thenReturn(loanRequest);
        when(loanRequestRepository.save(any(LoanRequest.class))).thenReturn(loanRequest);

        // Act
        LoanRequest updatedLoanRequest = loanRequestService.updateStatus(1L, LoanRequestStatus.APPROVED);

        // Assert
        assertNotNull(updatedLoanRequest);
        assertEquals(LoanRequestStatus.APPROVED, updatedLoanRequest.getStatus());
        verify(loanRequestRepository, times(1)).findById(1L);
        verify(loanRequestRepository, times(1)).save(loanRequest);
    }

    @Test
    void updateStatus_ShouldThrowException_WhenLoanRequestNotFound() {
        // Arrange
        when(loanRequestRepository.findById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                loanRequestService.updateStatus(999L, LoanRequestStatus.APPROVED)
        );
        verify(loanRequestRepository, times(1)).findById(999L);
        verify(loanRequestRepository, never()).updateStatus(anyLong(), any());
    }
}
