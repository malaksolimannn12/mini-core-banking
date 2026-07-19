package com.bank.minicorebanking.controller;

import com.bank.minicorebanking.dto.AccountRequestDTO;
import com.bank.minicorebanking.dto.AccountResponseDTO;
import com.bank.minicorebanking.dto.TransactionResponseDTO;
import com.bank.minicorebanking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public AccountResponseDTO createAccount(@Valid @RequestBody AccountRequestDTO dto) {
        return service.createAccount(dto);
    }

    @GetMapping
    public List<AccountResponseDTO> getAllAccounts() {
        return service.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountResponseDTO getAccountById(@PathVariable Long id) {
        return service.getAccountByIdDTO(id);
    }

    @PutMapping("/{id}")
    public AccountResponseDTO updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequestDTO dto) {
        return service.updateAccount(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
    }

    @PostMapping("/{id}/deposit")
    public AccountResponseDTO deposit(@PathVariable Long id, @RequestParam Double amount) {
        return service.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponseDTO withdraw(@PathVariable Long id, @RequestParam Double amount) {
        return service.withdraw(id, amount);
    }

    @PostMapping("/{fromId}/transfer")
    public void transfer(@PathVariable Long fromId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        service.transfer(fromId, toAccountId, amount);
    }

    @PostMapping("/{id}/interest")
    public AccountResponseDTO applyInterest(@PathVariable Long id, @RequestParam Double ratePercent) {
        return service.applyInterest(id, ratePercent);
    }

    @GetMapping("/{id}/statement")
    public List<TransactionResponseDTO> getStatement(
            @PathVariable Long id,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return service.getStatement(id, start, end);
    }
}