package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.mapper.ScheduledTransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.ScheduledTransaction;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.service.ScheduledTransactionService;
import com.fundmate.api.service.TransactionSchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduledTransactionServiceImpl implements ScheduledTransactionService {

    private final ScheduledTransactionRepository scheduledTransactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduledTransactionMapper scheduledTransactionMapper;
    private final TransactionSchedulerService transactionSchedulerService;

    public ScheduledTransactionServiceImpl(ScheduledTransactionRepository scheduledTransactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, ScheduledTransactionMapper scheduledTransactionMapper, TransactionSchedulerService transactionSchedulerService) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.scheduledTransactionMapper = scheduledTransactionMapper;
        this.transactionSchedulerService = transactionSchedulerService;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    public ScheduledTransactionResponse createScheduledTransaction(ScheduledTransactionRequest scheduledTransactionRequest) {
        Account account = accountRepository.findById(scheduledTransactionRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        Category category = categoryRepository.findById(scheduledTransactionRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId()) || !account.getUser().getId().equals(category.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (scheduledTransactionRequest.getExecutionDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Start date must be today, or in the future");
        }

        ScheduledTransaction scheduledTransaction = scheduledTransactionMapper.toEntity(scheduledTransactionRequest);
        scheduledTransaction.setAccount(account);
        scheduledTransaction.setCategory(category);

        ScheduledTransaction savedScheduledTransaction = scheduledTransactionRepository.save(scheduledTransaction);
        
        // Manually create transaction if due today on creation - think this will fix the issue
        if (transactionSchedulerService.isTransactionDue(savedScheduledTransaction, LocalDate.now())) {
            transactionSchedulerService.createTransaction(savedScheduledTransaction);
            transactionSchedulerService.updateOccurrences(savedScheduledTransaction);
        }
        return scheduledTransactionMapper.toResponse(savedScheduledTransaction);
    }

    @Override
    public List<ScheduledTransactionResponse> getAllScheduledTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return scheduledTransactionRepository.findByAccountId(accountId).stream()
                .map(scheduledTransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduledTransactionResponse getScheduledTransactionById(Long id) {
        ScheduledTransaction scheduledTransaction = scheduledTransactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheduled transaction not found"));

        if (!scheduledTransaction.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return scheduledTransactionMapper.toResponse(scheduledTransaction);
    }

    @Override
    public void deleteScheduledTransaction(Long id) {
        ScheduledTransaction transaction = scheduledTransactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheduled transaction not found"));

        if (!transaction.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        scheduledTransactionRepository.delete(transaction);

    }


}
