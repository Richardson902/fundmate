package com.fundmate.api.unit.transaction;

import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.mapper.TransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.Transaction;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest transactionRequest;
    private Transaction transaction;
    private TransactionResponse transactionResponse;
    private Account account;
    private Category category;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);
        account.setUser(user);

        category = new Category();
        category.setId(1L);
        category.setUser(user);

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(100.0);
        transactionRequest.setCategoryId(1L);
        transactionRequest.setAccountId(1L);
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setFromName("Test Sender");
        transactionRequest.setNote("Test Note");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setDate(LocalDate.now());
        transaction.setFromName("Test Sender");
        transaction.setNote("Test Note");

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(1L);
        transactionResponse.setAmount(100.0);
        transactionResponse.setDate(LocalDate.now());
        transactionResponse.setFromName("Test Sender");
        transactionResponse.setNote("Test Note");
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void createTransaction_ShouldCreateAndReturnTransaction() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionMapper.toEntity(transactionRequest)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(transactionResponse);

        TransactionResponse result = transactionService.createTransaction(transactionRequest);

        assertNotNull(result);
        assertEquals(transactionResponse.getId(), result.getId());
        assertEquals(transactionResponse.getAmount(), result.getAmount());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void createTransaction_WithUnauthorizedAccount_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.createTransaction(transactionRequest));
    }

    @Test
    void createTransaction_WithUnauthorizedCategory_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        category.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.createTransaction(transactionRequest));
    }

    @Test
    void getTransactionsByAccountId_ShouldReturnList() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountIdOrderByDateDesc(1L)).thenReturn(Arrays.asList(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(transactionResponse);

        List<TransactionResponse> result = transactionService.getTransactionByAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transactionResponse, result.get(0));
        verify(transactionRepository).findByAccountIdOrderByDateDesc(1L);
    }

    @Test
    void getTransactionsByAccountId_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.getTransactionByAccountId(1L));
    }

    @Test
    void getTransactionsByDateRange_ShouldReturnList() {
        setupSecurityContext();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountIdAndDateBetweenOrderByDateDesc(1L, startDate, endDate))
                .thenReturn(Arrays.asList(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(transactionResponse);

        List<TransactionResponse> result =
                transactionService.getTransactionsByDateRange(1L, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transactionResponse, result.get(0));
        verify(transactionRepository).findByAccountIdAndDateBetweenOrderByDateDesc(1L, startDate, endDate);
    }

    @Test
    void getTransactionsByDateRange_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.getTransactionsByDateRange(1L, LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    @Test
    void updateTransaction_ShouldUpdateAndReturnTransaction() {
        setupSecurityContext();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(transactionResponse);

        TransactionResponse result = transactionService.updateTransaction(1L, transactionRequest);

        assertNotNull(result);
        assertEquals(transactionResponse.getId(), result.getId());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).save(transaction);
    }

    @Test
    void updateTransaction_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.updateTransaction(1L, transactionRequest));
    }

    @Test
    void deleteTransaction_ShouldDeleteAndUpdateBalance() {
        setupSecurityContext();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L);

        verify(accountRepository).save(account);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    void deleteTransaction_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(ResponseStatusException.class, () ->
                transactionService.deleteTransaction(1L));
    }
}