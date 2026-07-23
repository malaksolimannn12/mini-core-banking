package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.AccountRequestDTO;
import com.bank.minicorebanking.dto.AccountResponseDTO;
import com.bank.minicorebanking.dto.TransactionResponseDTO;
import com.bank.minicorebanking.dto.TransactionSummaryDTO;
import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Customer;
import com.bank.minicorebanking.entity.Transaction;
import com.bank.minicorebanking.repository.AccountRepository;
import com.bank.minicorebanking.repository.CustomerRepository;
import com.bank.minicorebanking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository repo;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository repo, CustomerRepository customerRepository,
                          TransactionService transactionService, TransactionRepository transactionRepository) {
        this.repo = repo;
        this.customerRepository = customerRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO dto) {
        Optional<Account> existing = repo.findByAccountNumber(dto.getAccountNumber());
        if (existing.isPresent()) {
            throw new RuntimeException("Account already exists");
        }

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setAccountType(dto.getAccountType());
        account.setCustomer(customer);
        account.setApproved(false);

        Account saved = repo.save(account);
        return convertToDTO(saved);
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        return repo.findAll().stream()
                .filter(a -> a.getCustomer() != null && a.getCustomer().getId().equals(customerId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountByIdDTO(Long id) {
        return convertToDTO(getAccountById(id));
    }

    public Account getAccountById(Long id) {
        Optional<Account> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Account does not exist");
        } else {
            return existing.get();
        }
    }

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO dto) {
        Account accountToUpdate = getAccountById(id);
        accountToUpdate.setBalance(dto.getBalance());
        accountToUpdate.setAccountType(dto.getAccountType());
        accountToUpdate.setAccountNumber(dto.getAccountNumber());

        Account saved = repo.save(accountToUpdate);
        return convertToDTO(saved);
    }

    public void deleteAccount(Long id) {
        Optional<Account> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Account does not exist");
        } else {
            repo.deleteById(id);
        }
    }

    public AccountResponseDTO approveAccount(Long id) {
        Account acc = getAccountById(id);
        acc.setApproved(true);
        Account saved = repo.save(acc);
        return convertToDTO(saved);
    }

    // ---- Private helpers: pure balance logic, no logging ----

    private Account increaseBalance(Long accountId, Double amount) {
        Account acc = getAccountById(accountId);
        if (amount <= 0) {
            throw new RuntimeException("Amount cannot be zero or negative");
        }
        acc.setBalance(acc.getBalance() + amount);
        return repo.save(acc);
    }

    private Account decreaseBalance(Long accountId, Double amount) {
        Account acc = getAccountById(accountId);
        if (amount <= 0) {
            throw new RuntimeException("Amount cannot be zero or negative");
        }
        if (acc.getBalance() < amount) {
            throw new RuntimeException("insufficient balance");
        }
        acc.setBalance(acc.getBalance() - amount);
        return repo.save(acc);
    }

    // ---- Public operations: use helpers + log their own transaction type ----

    public AccountResponseDTO deposit(Long accountId, Double amount) {
        Account saved = increaseBalance(accountId, amount);
        transactionService.logTransaction(saved, "DEPOSIT", amount);
        return convertToDTO(saved);
    }

    public AccountResponseDTO withdraw(Long accountId, Double amount) {
        Account saved = decreaseBalance(accountId, amount);
        transactionService.logTransaction(saved, "WITHDRAW", amount);
        return convertToDTO(saved);
    }

    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        Account fromAcc = decreaseBalance(fromAccountId, amount);
        Account toAcc = increaseBalance(toAccountId, amount);

        transactionService.logTransaction(fromAcc, "TRANSFER_OUT", amount);
        transactionService.logTransaction(toAcc, "TRANSFER_IN", amount);
    }

    // ---- Interest ----

    public AccountResponseDTO applyInterest(Long accountId, Double ratePercent) {
        if (ratePercent <= 0) {
            throw new RuntimeException("Interest rate must be positive");
        }

        Account acc = getAccountById(accountId);
        Double interestAmount = acc.getBalance() * (ratePercent / 100);
        acc.setBalance(acc.getBalance() + interestAmount);

        Account saved = repo.save(acc);
        transactionService.logTransaction(saved, "INTEREST", interestAmount);
        return convertToDTO(saved);
    }

    // ---- Statement ----

    public List<TransactionResponseDTO> getStatement(Long accountId, LocalDateTime start, LocalDateTime end) {
        Account acc = getAccountById(accountId);
        List<Transaction> transactions = transactionRepository.findByAccountAndTimestampBetween(acc, start, end);

        return transactions.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(), t.getTransactionType(), t.getAmount(), t.getTimestamp(),
                        acc.getId(), acc.getAccountNumber()
                ))
                .collect(Collectors.toList());
    }

    // ---- DTO conversion ----

    private AccountResponseDTO convertToDTO(Account account) {
        List<TransactionSummaryDTO> transactionDTOs = account.getTransactions() == null
                ? new ArrayList<>()
                : account.getTransactions().stream()
                  .map(t -> new TransactionSummaryDTO(t.getId(), t.getTransactionType(), t.getAmount(), t.getTimestamp()))
                  .collect(Collectors.toList());

        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCustomer() != null ? account.getCustomer().getId() : null,
                transactionDTOs
        );
    }
}
//Let's go through this one line by line.
//
//## Method signature
//
//```java
//public List<TransactionResponseDTO> getStatement(Long accountId, LocalDateTime start, LocalDateTime end)
//```
//
//Takes 3 inputs: which account (`accountId`), and the date range (`start`, `end`). Returns a `List` of `TransactionResponseDTO` — the safe, clean version of each transaction we show to the outside world (never raw entities).
//
//## Line 1
//
//```java
//Account acc = getAccountById(accountId);
//```
//
//Fetches the real `Account` entity using the id. This reuses your existing method, which already handles "what if this account doesn't exist" internally (throws an error if not found) — so we don't need to repeat that check here.
//
//## Line 2
//
//```java
//List<Transaction> transactions = transactionRepository.findByAccountAndTimestampBetween(acc, start, end);
//```
//
//Calls the repository method — Spring auto-generates the SQL behind this, roughly: *"find every transaction where the account matches `acc`, AND the timestamp falls between `start` and `end`."* Returns a `List` (since many transactions could match a date range).
//
//## Lines 3-7 — the conversion
//
//```java
//return transactions.stream()
//        .map(t -> new TransactionResponseDTO(
//                t.getId(), t.getTransactionType(), t.getAmount(), t.getTimestamp(),
//                acc.getId(), acc.getAccountNumber()
//        ))
//        .collect(Collectors.toList());
//```
//
//This transforms the raw `List<Transaction>` into a `List<TransactionResponseDTO>`, one item at a time:
//
//- **`.stream()`** → turns the list into something you can process item-by-item
//- **`.map(t -> new TransactionResponseDTO(...))`** → for each transaction `t`, build a new DTO using: the transaction's own `id`, `transactionType`, `amount`, `timestamp` — plus the account's `id` and `accountNumber` (pulled from `acc`, which we already fetched, rather than re-fetching from `t` each time)
//- **`.collect(Collectors.toList())`** → gathers all those new DTOs into a real `List` to return
//
//## The overall purpose
//
//This method answers: *"Show me everything that happened on this specific account, between these two dates"* — exactly what a bank statement is. It reuses the exact same "stream → map → collect" pattern you've already used elsewhere (like `getAllAccounts()`), just filtered by account and date instead of getting everything.
//
//Does this make sense now? Want me to walk through `applyInterest` next, or something else?
    // ---- DTO conversion ----

