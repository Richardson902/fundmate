package com.fundmate.api.service.impl;

import com.fundmate.api.mapper.TransactionMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.ScheduledTransaction;
import com.fundmate.api.model.Transaction;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.TransactionSchedulerService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class TransactionSchedulerServiceImpl implements TransactionSchedulerService {

    private final ScheduledTransactionRepository scheduledTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionSchedulerServiceImpl(ScheduledTransactionRepository scheduledTransactionRepository, TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionMapper transactionMapper) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void ProcessScheduledTransactions() {
        LocalDate today = LocalDate.now();
        List<ScheduledTransaction> scheduledTransactions = scheduledTransactionRepository.findAll();

        for (ScheduledTransaction scheduledTransaction : scheduledTransactions) {
            if (isTransactionDue(scheduledTransaction, today)) {
                createTransaction(scheduledTransaction);
                updateOccurrences(scheduledTransaction);
            }
        }

    }

    @Override
    public boolean isTransactionDue(ScheduledTransaction scheduledTransaction, LocalDate today) {
        if (scheduledTransaction.getExecutionDate().isAfter(today) || scheduledTransaction.getOccurrences() <= 0) {
            return false;
        }

        long daysBetween = ChronoUnit.DAYS.between(scheduledTransaction.getExecutionDate(), today);

        return switch (scheduledTransaction.getRecurrenceType()) {
            case DAILY -> daysBetween % scheduledTransaction.getRecurrenceInterval() == 0;
            case WEEKLY -> daysBetween % (7L * scheduledTransaction.getRecurrenceInterval()) == 0;
            case MONTHLY -> scheduledTransaction.getExecutionDate().getDayOfMonth() == today.getDayOfMonth() &&
                    daysBetween >= 28L * scheduledTransaction.getRecurrenceInterval();
            case YEARLY -> scheduledTransaction.getExecutionDate().getDayOfYear() == today.getDayOfYear() &&
                    daysBetween >= 365L * scheduledTransaction.getRecurrenceInterval();
        };
    }

    @Override
    public void createTransaction(ScheduledTransaction scheduledTransaction) {
        Transaction transaction = new Transaction();
        transaction.setAmount(scheduledTransaction.getAmount());
        transaction.setAccount(scheduledTransaction.getAccount());
        transaction.setCategory(scheduledTransaction.getCategory());
        transaction.setDate(LocalDate.now());
        transaction.setFromName(scheduledTransaction.getFromName());
        transaction.setNote(scheduledTransaction.getNote());

        Account account = scheduledTransaction.getAccount();
        account.setBalance(account.getBalance() + scheduledTransaction.getAmount());

        scheduledTransaction.setExecutionDate(LocalDate.now());
        scheduledTransactionRepository.save(scheduledTransaction);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    public void updateOccurrences(ScheduledTransaction scheduledTransaction) {
        scheduledTransaction.setOccurrences(scheduledTransaction.getOccurrences() - 1);
        if (scheduledTransaction.getOccurrences() <= 0) {
            scheduledTransactionRepository.delete(scheduledTransaction);
        } else {
            scheduledTransactionRepository.save(scheduledTransaction);
        }

    }
}
