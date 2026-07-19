package com.bank.minicorebanking.repository;

import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
    List<Transaction> findByAccountAndTimestampBetween(Account account, LocalDateTime start, LocalDateTime end);
//transaction repo method is method that i will give it the date and the account and it will return for me all the transaction of this account that have been applied on this account
}
//Yes — exactly right. Let's confirm this precisely.
//
//## Your understanding, confirmed
//
//```java
//List<Transaction> findByAccountAndTimestampBetween(Account account, LocalDateTime start, LocalDateTime end);
//```
//
//You give it:
//- **`account`** → which account you're asking about
//- **`start`** and **`end`** → the date range you care about
//
//It gives you back:
//- **A `List<Transaction>`** → every transaction on that account, but *only* the ones that happened between those two dates
//
//## Breaking down the method name itself (how Spring parses it)
//
//- **`findBy`** → "search for records where..."
//- **`Account`** → "...the `account` field matches what I gave you..."
//- **`And`** → "...AND ALSO..."
//- **`TimestampBetween`** → "...the `timestamp` field falls between two values I'll provide"
//
//Spring reads this name, recognizes the pattern, and automatically builds the equivalent SQL:
//```sql
//SELECT * FROM transactions
//WHERE account_id = ? AND timestamp BETWEEN ? AND ?
//```
//
//You never write that SQL — Spring generates it just from the method's name and its parameter types.
//
//## Why `List`, not `Optional`
//
//Since a date range could match **many** transactions (or zero, or one) — remember our earlier rule: "many possible results → `List`, at most one result → `Optional`." A statement over a month could easily have 10+ transactions, so `List` is the correct choice here.
