package com.fundmate.api.unit.scheduledtransaction;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.mapper.ScheduledTransactionMapper;
import com.fundmate.api.model.*;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.service.impl.ScheduledTransactionServiceImpl;
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
class ScheduledTransactionServiceImplTest {

    @Mock
    private ScheduledTransactionRepository scheduledTransactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ScheduledTransactionMapper scheduledTransactionMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ScheduledTransactionServiceImpl scheduledTransactionService;

    private ScheduledTransactionRequest request;
    private ScheduledTransaction scheduledTransaction;
    private ScheduledTransactionResponse response;
    private Account account;
    private Category category;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        account = new Account();
        account.setId(1L);
        account.setAccountName("Test Account");
        account.setBalance(1000.0);
        account.setUser(user);

        category = new Category();
        category.setId(1L);
        category.setCategoryName("Test Category");
        category.setUser(user);

        request = new ScheduledTransactionRequest();
        request.setAmount(100.0);
        request.setAccountId(1L);
        request.setCategoryId(1L);
        request.setExecutionDate(LocalDate.now());
        request.setRecurrenceType(RecurrenceType.MONTHLY);
        request.setRecurrenceInterval(1);
        request.setOccurrences(12);
        request.setFromName("Test Source");
        request.setNote("Test Note");

        scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setId(1L);
        scheduledTransaction.setAmount(100.0);
        scheduledTransaction.setAccount(account);
        scheduledTransaction.setCategory(category);
        scheduledTransaction.setExecutionDate(LocalDate.now());
        scheduledTransaction.setRecurrenceType(RecurrenceType.MONTHLY);
        scheduledTransaction.setRecurrenceInterval(1);
        scheduledTransaction.setOccurrences(12);
        scheduledTransaction.setFromName("Test Source");
        scheduledTransaction.setNote("Test Note");

        response = new ScheduledTransactionResponse();
        response.setId(1L);
        response.setAmount(100.0);
        response.setExecutionDate(LocalDate.now());
        response.setRecurrenceType(RecurrenceType.MONTHLY);
        response.setRecurrenceInterval(1);
        response.setOccurrences(12);
        response.setFromName("Test Source");
        response.setNote("Test Note");
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void createScheduledTransaction_ShouldCreateAndReturnTransaction() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(scheduledTransactionMapper.toEntity(request)).thenReturn(scheduledTransaction);
        when(scheduledTransactionRepository.save(any(ScheduledTransaction.class)))
                .thenReturn(scheduledTransaction);
        when(scheduledTransactionMapper.toResponse(scheduledTransaction)).thenReturn(response);

        ScheduledTransactionResponse result = scheduledTransactionService.createScheduledTransaction(request);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getAmount(), result.getAmount());
        verify(scheduledTransactionRepository).save(scheduledTransaction);
    }

    @Test
    void createScheduledTransaction_WithUnauthorizedAccount_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.createScheduledTransaction(request));
    }

    @Test
    void createScheduledTransaction_WithUnauthorizedCategory_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        category.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.createScheduledTransaction(request));
    }

    @Test
    void createScheduledTransaction_WithPastDate_ShouldThrowException() {
        request.setExecutionDate(LocalDate.now().minusDays(1));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.createScheduledTransaction(request));
    }

    @Test
    void getAllScheduledTransactionsByAccountId_ShouldReturnList() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(scheduledTransactionRepository.findByAccountId(1L))
                .thenReturn(Arrays.asList(scheduledTransaction));
        when(scheduledTransactionMapper.toResponse(scheduledTransaction)).thenReturn(response);

        List<ScheduledTransactionResponse> result =
                scheduledTransactionService.getAllScheduledTransactionsByAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(scheduledTransactionRepository).findByAccountId(1L);
    }

    @Test
    void getAllScheduledTransactionsByAccountId_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.getAllScheduledTransactionsByAccountId(1L));
    }

    @Test
    void getScheduledTransactionById_ShouldReturnTransaction() {
        setupSecurityContext();
        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));
        when(scheduledTransactionMapper.toResponse(scheduledTransaction)).thenReturn(response);

        ScheduledTransactionResponse result = scheduledTransactionService.getScheduledTransactionById(1L);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        verify(scheduledTransactionRepository).findById(1L);
    }

    @Test
    void getScheduledTransactionById_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.getScheduledTransactionById(1L));
    }

    @Test
    void getScheduledTransactionById_WithInvalidId_ShouldThrowException() {
        when(scheduledTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.getScheduledTransactionById(1L));
    }

    @Test
    void deleteScheduledTransaction_ShouldDelete() {
        setupSecurityContext();
        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));

        scheduledTransactionService.deleteScheduledTransaction(1L);

        verify(scheduledTransactionRepository).delete(scheduledTransaction);
    }

    @Test
    void deleteScheduledTransaction_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        account.setUser(otherUser);

        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.deleteScheduledTransaction(1L));
    }

    @Test
    void deleteScheduledTransaction_WithInvalidId_ShouldThrowException() {
        when(scheduledTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.deleteScheduledTransaction(1L));
    }
}