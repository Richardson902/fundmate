package com.fundmate.api.unit.transaction;

import com.fundmate.api.controller.TransactionController;
import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.service.TransactionService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(100.0);
        transactionRequest.setCategoryId(1L);
        transactionRequest.setAccountId(1L);
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setFromName("Test Sender");
        transactionRequest.setNote("Test Note");

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(1L);
        transactionResponse.setAmount(100.0);
        transactionResponse.setDate(LocalDate.now());
        transactionResponse.setFromName("Test Sender");
        transactionResponse.setNote("Test Note");
    }

    @Test
    void createTransaction_ShouldReturnCreatedResponse() {
        when(transactionService.createTransaction(any(TransactionRequest.class)))
                .thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = transactionController.createTransaction(transactionRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(transactionService).createTransaction(transactionRequest);
    }

    @Test
    void getTransactionsByAccountId_ShouldReturnList() {
        List<TransactionResponse> transactions = Arrays.asList(transactionResponse);
        when(transactionService.getTransactionByAccountId(1L)).thenReturn(transactions);

        ResponseEntity<List<TransactionResponse>> response = transactionController.getTransactionsByAccountId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transactionService).getTransactionByAccountId(1L);
    }

    @Test
    void getTransactionsByDateRange_ShouldReturnList() {
        List<TransactionResponse> transactions = Arrays.asList(transactionResponse);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);

        when(transactionService.getTransactionsByDateRange(eq(1L), eq(startDate), eq(endDate)))
                .thenReturn(transactions);

        ResponseEntity<List<TransactionResponse>> response =
                transactionController.getTransactionsByDateRange(1L, startDate, endDate);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService).getTransactionsByDateRange(1L, startDate, endDate);
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        when(transactionService.getTransactionById(1L)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response = transactionController.getTransactionById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(transactionService).getTransactionById(1L);
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        when(transactionService.updateTransaction(eq(1L), any(TransactionRequest.class)))
                .thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> response =
                transactionController.updateTransaction(1L, transactionRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());
        verify(transactionService).updateTransaction(1L, transactionRequest);
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService).deleteTransaction(1L);
    }
}