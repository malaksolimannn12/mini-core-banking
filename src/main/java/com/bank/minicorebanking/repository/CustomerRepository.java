package com.bank.minicorebanking.repository;

import com.bank.minicorebanking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

}
//interface not class — a repository in Spring is a contract, not something you build logic inside. You're saying "I need these database operations," and Spring auto-generates the actual working code behind the scenes at runtime. You never write the implementation yourself.
//JpaRepository<Customer, Long> — this is a generic template. Customer = which entity this repository manages. Long = the data type of that entity's @Id field (your id is a Long).
//Just by extending JpaRepository, you instantly get free methods: save(), findAll(), findById(), deleteById(), and more — zero SQL written by yo