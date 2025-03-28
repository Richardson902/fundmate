package com.fundmate.api.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponse {
    private Long id;
    private Double amount;
    private CategoryResponse category;
    private AccountResponse account;
    private LocalDate date;
    private String fromName;
    private String note;
}
