package com.fundmate.api.unit.scheduledtransaction;

import com.fundmate.api.controller.ScheduleTransactionController;
import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.model.RecurrenceType;
import com.fundmate.api.service.ScheduledTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTransactionControllerTest {

    @Mock
    private ScheduledTransactionService scheduledTransactionService;

    @InjectMocks
    private ScheduleTransactionController scheduleTransactionController;

    private ScheduledTransactionRequest request;
    private ScheduledTransactionResponse response;

    @BeforeEach
    void setUp() {
        request = new ScheduledTransactionRequest();
        request.setAmount(100.0);
        request.setAccountId(1L);
        request.setCategoryId(1L);
        request.setStartDate(LocalDate.now());
        request.setRecurrenceType(RecurrenceType.MONTHLY);
        request.setRecurrenceInterval(1);
        request.setOccurrences(12);
        request.setFromName("Test Source");
        request.setNote("Test Note");

        response = new ScheduledTransactionResponse();
        response.setId(1L);
        response.setAmount(100.0);
        response.setStartDate(LocalDate.now());
        response.setRecurrenceType(RecurrenceType.MONTHLY);
        response.setRecurrenceInterval(1);
        response.setOccurrences(12);
        response.setFromName("Test Source");
        response.setNote("Test Note");
    }

    @Test
    void scheduleTransaction_ShouldReturnCreated() {
        when(scheduledTransactionService.createScheduledTransaction(any(ScheduledTransactionRequest.class)))
                .thenReturn(response);

        ResponseEntity<ScheduledTransactionResponse> result = scheduleTransactionController.scheduleTransaction(request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(scheduledTransactionService).createScheduledTransaction(request);
    }

    @Test
    void getScheduledTransactionByAccountId_ShouldReturnList() {
        List<ScheduledTransactionResponse> transactions = Arrays.asList(response);
        when(scheduledTransactionService.getAllScheduledTransactionsByAccountId(1L))
                .thenReturn(transactions);

        ResponseEntity<List<ScheduledTransactionResponse>> result =
                scheduleTransactionController.getScheduledTransactionByAccountId(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(transactions, result.getBody());
        assertEquals(1, result.getBody().size());
        verify(scheduledTransactionService).getAllScheduledTransactionsByAccountId(1L);
    }

    @Test
    void getScheduledTransactionById_ShouldReturnTransaction() {
        when(scheduledTransactionService.getScheduledTransactionById(1L)).thenReturn(response);

        ResponseEntity<ScheduledTransactionResponse> result =
                scheduleTransactionController.getScheduledTransactionById(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(scheduledTransactionService).getScheduledTransactionById(1L);
    }

    @Test
    void deleteScheduledTransactionById_ShouldReturnNoContent() {
        doNothing().when(scheduledTransactionService).deleteScheduledTransaction(1L);

        ResponseEntity<Void> result = scheduleTransactionController.deleteScheduledTransactionById(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(scheduledTransactionService).deleteScheduledTransaction(1L);
    }
}