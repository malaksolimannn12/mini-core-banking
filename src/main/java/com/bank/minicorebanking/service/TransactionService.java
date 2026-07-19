package com.bank.minicorebanking.service;

import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Transaction;
import com.bank.minicorebanking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public void logTransaction(Account account, String type, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        repo.save(transaction);

    }
}