package com.fundmate.api.service;

import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getTransactionByAccountId(Long accountId);
    List<TransactionResponse> getTransactionsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);
    TransactionResponse getTransactionById(Long id);
    TransactionResponse updateTransaction(Long id, TransactionRequest transactionRequest);
    void deleteTransaction(Long id);
}
