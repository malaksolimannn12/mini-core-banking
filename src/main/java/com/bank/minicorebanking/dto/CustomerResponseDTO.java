package com.bank.minicorebanking.dto;

import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<String> accountNumbers;

}