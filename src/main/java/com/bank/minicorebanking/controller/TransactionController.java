package com.bank.minicorebanking.controller;

import com.bank.minicorebanking.dto.TransactionResponseDTO;
import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Transaction;
import com.bank.minicorebanking.repository.TransactionRepository;
import com.bank.minicorebanking.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionController(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponseDTO> getTransactionsForAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        List<Transaction> transactions = transactionRepository.findByAccount(account);

        return transactions.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getTimestamp(),
                        account.getId(),
                        account.getAccountNumber()
                ))
                .collect(Collectors.toList());
    }
}
