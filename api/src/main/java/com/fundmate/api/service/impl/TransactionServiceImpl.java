package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.mapper.TransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.Transaction;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,AccountRepository accountRepository,CategoryRepository categoryRepository,TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        Account account = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = categoryRepository.findById(transactionRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Transaction transaction = transactionMapper.toEntity(transactionRequest);
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);

        account.setBalance(account.getBalance() + transactionRequest.getAmount());
        accountRepository.save(account);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public List<TransactionResponse> getTransactionByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByAccountIdAndDateBetween(accountId, startDate, endDate).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        return transactionMapper.toResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest transactionRequest) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Account newAccount = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = categoryRepository.findById(transactionRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Revert the old transaction amount from account balance
        Account oldAccount = existingTransaction.getAccount();
        oldAccount.setBalance(oldAccount.getBalance() - transactionRequest.getAmount());
        accountRepository.save(oldAccount);

        // Update transaction fields
        existingTransaction.setAmount(transactionRequest.getAmount());
        existingTransaction.setCategory(category);
        existingTransaction.setAccount(newAccount);
        existingTransaction.setDate(transactionRequest.getDate());
        existingTransaction.setFromName(transactionRequest.getFromName());
        existingTransaction.setNote(transactionRequest.getNote());

        // Add new amount to account balance
        newAccount.setBalance(newAccount.getBalance() - transactionRequest.getAmount());
        accountRepository.save(newAccount);

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        // Update account balance
        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }
}
