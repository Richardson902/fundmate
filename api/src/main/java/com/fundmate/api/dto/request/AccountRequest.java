package com.fundmate.api.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String accountName;
    private Double balance;
}
