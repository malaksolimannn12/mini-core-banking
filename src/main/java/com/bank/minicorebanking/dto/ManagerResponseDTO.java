package com.bank.minicorebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponseDTO {
    private Long id;
    private String fullName;
    private String email;
}