package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.mapper.ScheduledTransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.ScheduledTransaction;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.service.ScheduledTransactionService;
import org.springframework.http.HttpStatus;
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

    public ScheduledTransactionServiceImpl(ScheduledTransactionRepository scheduledTransactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, ScheduledTransactionMapper scheduledTransactionMapper) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.scheduledTransactionMapper = scheduledTransactionMapper;
    }


    @Override
    public ScheduledTransactionResponse createScheduledTransaction(ScheduledTransactionRequest scheduledTransactionRequest) {
        Account account = accountRepository.findById(scheduledTransactionRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        Category category = categoryRepository.findById(scheduledTransactionRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (scheduledTransactionRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Start date must be today, or in the future");
        }

        ScheduledTransaction scheduledTransaction = scheduledTransactionMapper.toEntity(scheduledTransactionRequest);
        scheduledTransaction.setAccount(account);
        scheduledTransaction.setCategory(category);

        ScheduledTransaction savedScheduledTransaction = scheduledTransactionRepository.save(scheduledTransaction);
        return scheduledTransactionMapper.toResponse(savedScheduledTransaction);
    }

    @Override
    public List<ScheduledTransactionResponse> getAllScheduledTransactionsByAccountId(Long accountId) {
        return scheduledTransactionRepository.findByAccountId(accountId).stream()
                .map(scheduledTransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduledTransactionResponse getScheduledTransactionById(Long id) {
        ScheduledTransaction scheduledTransaction = scheduledTransactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheduled transaction not found"));
        return scheduledTransactionMapper.toResponse(scheduledTransaction);
    }

    @Override
    public void deleteScheduledTransaction(Long id) {
        ScheduledTransaction transaction = scheduledTransactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheduled transaction not found"));

        scheduledTransactionRepository.delete(transaction);

    }


}
