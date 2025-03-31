package com.fundmate.api.dto.request;

import com.fundmate.api.model.RecurrenceType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduledTransactionRequest {
    private Double amount;
    private Long categoryId;
    private Long accountId;
    private LocalDate executionDate;
    private RecurrenceType recurrenceType;
    private Integer recurrenceInterval;
    private Integer occurrences;
    private String fromName;
    private String note;
}
