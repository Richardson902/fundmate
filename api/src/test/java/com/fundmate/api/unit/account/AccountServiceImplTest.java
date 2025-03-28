package com.fundmate.api.unit.account;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.mapper.AccountMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.UserRepository;
import com.fundmate.api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountRequest accountRequest;
    private Account account;
    private AccountResponse accountResponse;
    private User user;
    private Long userId;
    private Long accountId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        accountId = 1L;

        accountRequest = new AccountRequest();
        accountRequest.setAccountName("Test Account");
        accountRequest.setBalance(1000.0);

        user = new User();
        user.setId(userId);

        account = new Account();
        account.setId(accountId);
        account.setAccountName("Test Account");
        account.setBalance(1000.0);
        account.setUser(user);

        accountResponse = new AccountResponse();
        accountResponse.setId(accountId);
        accountResponse.setAccountName("Test Account");
        accountResponse.setBalance(1000.0);
    }

    @Test
    void createAccount_ShouldCreateAndReturnAccount() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accountMapper.toEntity(accountRequest)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse result = accountService.createAccount(accountRequest, userId);

        assertNotNull(result);
        assertEquals(accountResponse.getId(), result.getId());
        assertEquals(accountResponse.getAccountName(), result.getAccountName());
        verify(accountRepository).save(account);
    }

    @Test
    void createAccount_WithInvalidUser_ShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                accountService.createAccount(accountRequest, userId));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountsByUserId_ShouldReturnAccounts() {
        List<Account> accounts = Arrays.asList(account);
        when(accountRepository.findByUserId(userId)).thenReturn(accounts);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        List<AccountResponse> result = accountService.getAccountsByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountResponse, result.get(0));
        verify(accountRepository).findByUserId(userId);
    }

    @Test
    void getAccountById_ShouldReturnAccount() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse result = accountService.getAccountById(accountId);

        assertNotNull(result);
        assertEquals(accountResponse, result);
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountById_WithInvalidId_ShouldThrowException() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                accountService.getAccountById(accountId));
    }

    @Test
    void updateAccount_ShouldUpdateAndReturnAccount() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse result = accountService.updateAccount(accountId, accountRequest);

        assertNotNull(result);
        assertEquals(accountResponse, result);
        verify(accountRepository).save(account);
    }

    @Test
    void updateAccount_WithInvalidId_ShouldThrowException() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                accountService.updateAccount(accountId, accountRequest));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccount_ShouldDeleteAccount() {
        accountService.deleteAccount(accountId);

        verify(accountRepository).deleteById(accountId);
    }
}