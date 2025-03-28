package com.fundmate.api.service;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;

import java.util.List;

public interface ScheduledTransactionService {
    ScheduledTransactionResponse createScheduledTransaction(ScheduledTransactionRequest scheduledTransactionRequest);
    List<ScheduledTransactionResponse> getAllScheduledTransactionsByAccountId(Long accountId);
    ScheduledTransactionResponse getScheduledTransactionById(Long id);
    void deleteScheduledTransaction(Long id);
}
