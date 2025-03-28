package com.fundmate.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduledTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private LocalDate startDate;
    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;
    private Integer recurrenceInterval;
    private Integer occurrences;
    private String fromName;
    private String note;
}
