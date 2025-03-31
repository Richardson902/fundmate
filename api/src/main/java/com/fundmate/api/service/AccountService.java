package com.fundmate.api.service;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    List<AccountResponse> getAccounts();
    AccountResponse getAccountById(Long accountId);
    AccountResponse updateAccount(Long accountId, AccountRequest accountRequest);
    void deleteAccount(Long accountId);
}
