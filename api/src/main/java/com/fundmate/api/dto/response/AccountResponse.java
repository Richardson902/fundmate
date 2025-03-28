package com.fundmate.api.dto.response;

import lombok.Data;

@Data
public class AccountResponse {
    private Long id;
    private String accountName;
    private Double balance;
}
