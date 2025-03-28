package com.fundmate.api.unit.scheduledtransaction;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.mapper.ScheduledTransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.RecurrenceType;
import com.fundmate.api.model.ScheduledTransaction;
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

    @InjectMocks
    private ScheduledTransactionServiceImpl scheduledTransactionService;

    private ScheduledTransactionRequest request;
    private ScheduledTransaction scheduledTransaction;
    private ScheduledTransactionResponse response;
    private Account account;
    private Category category;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountName("Test Account");
        account.setBalance(1000.0);

        category = new Category();
        category.setId(1L);
        category.setCategoryName("Test Category");

        request = new ScheduledTransactionRequest();
        request.setAmount(100.0);
        request.setAccountId(1L);
        request.setCategoryId(1L);
        request.setStartDate(LocalDate.now());
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
        scheduledTransaction.setStartDate(LocalDate.now());
        scheduledTransaction.setRecurrenceType(RecurrenceType.MONTHLY);
        scheduledTransaction.setRecurrenceInterval(1);
        scheduledTransaction.setOccurrences(12);
        scheduledTransaction.setFromName("Test Source");
        scheduledTransaction.setNote("Test Note");

        response = new ScheduledTransactionResponse();
        response.setId(1L);
        response.setAmount(100.0);
        response.setStartDate(LocalDate.now());
        response.setRecurrenceType(RecurrenceType.MONTHLY);
        response.setRecurrenceInterval(1);
        response.setOccurrences(12);
        response.setFromName("Test Source");
        response.setNote("Test Note");
    }

    @Test
    void createScheduledTransaction_ShouldCreateAndReturnTransaction() {
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
    void createScheduledTransaction_WithPastDate_ShouldThrowException() {
        request.setStartDate(LocalDate.now().minusDays(1));

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.createScheduledTransaction(request));
    }

    @Test
    void getAllScheduledTransactionsByAccountId_ShouldReturnList() {
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
    void getScheduledTransactionById_ShouldReturnTransaction() {
        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));
        when(scheduledTransactionMapper.toResponse(scheduledTransaction)).thenReturn(response);

        ScheduledTransactionResponse result = scheduledTransactionService.getScheduledTransactionById(1L);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        verify(scheduledTransactionRepository).findById(1L);
    }

    @Test
    void getScheduledTransactionById_WithInvalidId_ShouldThrowException() {
        when(scheduledTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.getScheduledTransactionById(1L));
    }

    @Test
    void deleteScheduledTransaction_ShouldDelete() {
        when(scheduledTransactionRepository.findById(1L))
                .thenReturn(Optional.of(scheduledTransaction));

        scheduledTransactionService.deleteScheduledTransaction(1L);

        verify(scheduledTransactionRepository).delete(scheduledTransaction);
    }

    @Test
    void deleteScheduledTransaction_WithInvalidId_ShouldThrowException() {
        when(scheduledTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                scheduledTransactionService.deleteScheduledTransaction(1L));
    }
}