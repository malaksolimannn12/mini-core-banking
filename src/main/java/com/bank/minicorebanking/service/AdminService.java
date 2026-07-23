package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.AdminRequestDTO;
import com.bank.minicorebanking.dto.AdminResponseDTO;
import com.bank.minicorebanking.entity.Admin;
import com.bank.minicorebanking.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminResponseDTO createAdmin(AdminRequestDTO dto) {
        Optional<Admin> existing = repo.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Admin admin = new Admin();
        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        Admin saved = repo.save(admin);
        return convertToDTO(saved);
    }

    public List<AdminResponseDTO> getAllAdmins() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin does not exist"));
        return convertToDTO(admin);
    }

    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
        Admin admin = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin does not exist"));

        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        Admin saved = repo.save(admin);
        return convertToDTO(saved);
    }

    public void deleteAdmin(Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new RuntimeException("Admin does not exist");
        }
        repo.deleteById(id);
    }

    private AdminResponseDTO convertToDTO(Admin admin) {
        return new AdminResponseDTO(admin.getId(), admin.getFullName(), admin.getEmail());
    }
}