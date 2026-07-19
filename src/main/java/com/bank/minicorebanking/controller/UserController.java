package com.bank.minicorebanking.controller;

import com.bank.minicorebanking.dto.LoginRequestDTO;
import com.bank.minicorebanking.dto.UserRequestDTO;
import com.bank.minicorebanking.dto.UserResponseDTO;
import com.bank.minicorebanking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody UserRequestDTO dto) {
        return service.registerUser(dto);
    }

    @PostMapping("/login")
    public UserResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return service.login(dto);
    }
}