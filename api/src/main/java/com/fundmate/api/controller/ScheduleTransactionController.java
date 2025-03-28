package com.fundmate.api.controller;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.service.ScheduledTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled-transactions")
public class ScheduleTransactionController {

    private final ScheduledTransactionService scheduledTransactionService;

    public ScheduleTransactionController(ScheduledTransactionService scheduledTransactionService) {
        this.scheduledTransactionService = scheduledTransactionService;
    }

    @PostMapping
    public ResponseEntity<ScheduledTransactionResponse> scheduleTransaction(@RequestBody ScheduledTransactionRequest request) {
        return new ResponseEntity<>(scheduledTransactionService.createScheduledTransaction(request), HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ScheduledTransactionResponse>> getScheduledTransactionByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(scheduledTransactionService.getAllScheduledTransactionsByAccountId(accountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTransactionResponse> getScheduledTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledTransactionService.getScheduledTransactionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledTransactionById(@PathVariable Long id) {
        scheduledTransactionService.deleteScheduledTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
