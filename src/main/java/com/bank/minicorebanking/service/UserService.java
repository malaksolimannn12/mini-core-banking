package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.LoginRequestDTO;
import com.bank.minicorebanking.dto.UserRequestDTO;
import com.bank.minicorebanking.dto.UserResponseDTO;
import com.bank.minicorebanking.entity.Customer;
import com.bank.minicorebanking.entity.Role;
import com.bank.minicorebanking.entity.User;
import com.bank.minicorebanking.repository.CustomerRepository;
import com.bank.minicorebanking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(UserRequestDTO dto) {
        Optional<User> existing = repo.findByUsername(dto.getUsername());
        if (existing.isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            user.setCustomer(customer);
        }

        User saved = repo.save(user);
        return convertToDTO(saved);
    }

    public UserResponseDTO login(LoginRequestDTO dto) {
        User user = repo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return convertToDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return convertToDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        Optional<User> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("User does not exist");
        } else {
            repo.deleteById(id);
        }
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User userToUpdate = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        userToUpdate.setUsername(dto.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(dto.getPassword()));
        userToUpdate.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            userToUpdate.setCustomer(customer);
        }

        User saved = repo.save(userToUpdate);
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
}