package com.bank.minicorebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String transactionType;
    private Double amount;
    private LocalDateTime timestamp;
    private Long accountId;
    private String accountNumber;
}