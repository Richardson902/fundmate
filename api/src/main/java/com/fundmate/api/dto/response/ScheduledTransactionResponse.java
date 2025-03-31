package com.fundmate.api.dto.response;

import com.fundmate.api.model.RecurrenceType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduledTransactionResponse {
    private Long id;
    private Double amount;
    private CategoryResponse category;
    private AccountResponse account;
    private LocalDate executionDate;
    private RecurrenceType recurrenceType;
    private Integer recurrenceInterval;
    private Integer occurrences;
    private String fromName;
    private String note;
}
