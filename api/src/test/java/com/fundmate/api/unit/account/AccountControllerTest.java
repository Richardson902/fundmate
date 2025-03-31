package com.fundmate.api.unit.account;

import com.fundmate.api.controller.AccountController;
import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private Long userId;
    private Long accountId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        accountId = 1L;

        accountRequest = new AccountRequest();
        accountRequest.setAccountName("Test Account");
        accountRequest.setBalance(1000.0);

        accountResponse = new AccountResponse();
        accountResponse.setId(accountId);
        accountResponse.setAccountName("Test Account");
        accountResponse.setBalance(1000.0);
    }

    @Test
    void createAccount_ShouldReturnCreatedResponse() {
        when(accountService.createAccount(any(AccountRequest.class)))
                .thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.createAccount(accountRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accountResponse, response.getBody());
        verify(accountService).createAccount(accountRequest);
    }

    @Test
    void getAccountsByUserId_ShouldReturnListOfAccounts() {
        List<AccountResponse> accounts = Arrays.asList(accountResponse);
        when(accountService.getAccounts()).thenReturn(accounts);

        ResponseEntity<List<AccountResponse>> response = accountController.getAccounts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(accountService).getAccounts();
    }

    @Test
    void getAccountById_ShouldReturnAccount() {
        when(accountService.getAccountById(accountId)).thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.getAccountById(accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountResponse, response.getBody());
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() {
        when(accountService.updateAccount(eq(accountId), any(AccountRequest.class)))
                .thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.updateAccount(accountId, accountRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountResponse, response.getBody());
        verify(accountService).updateAccount(accountId, accountRequest);
    }

    @Test
    void deleteAccount_ShouldReturnNoContent() {
        doNothing().when(accountService).deleteAccount(accountId);

        ResponseEntity<Void> response = accountController.deleteAccount(accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).deleteAccount(accountId);
    }
}