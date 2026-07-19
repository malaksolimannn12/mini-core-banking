package com.bank.minicorebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private Double balance;
    private String accountType;
    private Long customerId;
    private List<TransactionSummaryDTO> transactions;
}