package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.LoginRequestDTO;
import com.bank.minicorebanking.dto.UserRequestDTO;
import com.bank.minicorebanking.dto.UserResponseDTO;
import com.bank.minicorebanking.entity.Customer;
import com.bank.minicorebanking.entity.Role;
import com.bank.minicorebanking.entity.User;
import com.bank.minicorebanking.repository.CustomerRepository;
import com.bank.minicorebanking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final CustomerRepository customerRepository;

    public UserService(UserRepository repo, CustomerRepository customerRepository) {
        this.repo = repo;
        this.customerRepository = customerRepository;
    }

    public UserResponseDTO registerUser(UserRequestDTO dto) {
        Optional<User> existing = repo.findByUsername(dto.getUsername());
        if (existing.isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: hash this once Security is added back
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            user.setCustomer(customer);
        }

        User saved = repo.save(user);
        return convertToDTO(saved);
    }

    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getCustomer() != null ? user.getCustomer().getId() : null
        );
    }
    public UserResponseDTO login(LoginRequestDTO dto) {
        User user = repo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return convertToDTO(user);
    }
}