package com.fundmate.api.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequest {
    private Double amount;
    private Long categoryId;
    private Long accountId;
    private LocalDate date;
    private String fromName;
    private String note;
}
