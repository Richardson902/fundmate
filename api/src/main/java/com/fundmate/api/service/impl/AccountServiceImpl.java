package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.mapper.AccountMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.UserRepository;
import com.fundmate.api.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        account.setUser(getCurrentUser());
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public List<AccountResponse> getAccounts() {
        return accountRepository.findByUserId(getCurrentUser().getId()).stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return accountMapper.toResponse(account);
    }

    @Override
    public AccountResponse updateAccount(Long accountId, AccountRequest accountRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        account.setAccountName(accountRequest.getAccountName());
        account.setBalance(accountRequest.getBalance());

        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        accountRepository.deleteById(accountId);
    }
}

