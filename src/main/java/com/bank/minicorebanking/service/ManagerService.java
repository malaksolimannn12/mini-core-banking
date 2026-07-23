package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.ManagerRequestDTO;
import com.bank.minicorebanking.dto.ManagerResponseDTO;
import com.bank.minicorebanking.entity.Manager;
import com.bank.minicorebanking.repository.ManagerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerRepository repo;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public ManagerResponseDTO createManager(ManagerRequestDTO dto) {
        Optional<Manager> existing = repo.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Manager manager = new Manager();
        manager.setFullName(dto.getFullName());
        manager.setEmail(dto.getEmail());
        manager.setPassword(passwordEncoder.encode(dto.getPassword()));

        Manager saved = repo.save(manager);
        return convertToDTO(saved);
    }

    public List<ManagerResponseDTO> getAllManagers() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ManagerResponseDTO getManagerById(Long id) {
        Manager manager = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager does not exist"));
        return convertToDTO(manager);
    }

    public ManagerResponseDTO updateManager(Long id, ManagerRequestDTO dto) {
        Manager manager = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager does not exist"));

        manager.setFullName(dto.getFullName());
        manager.setEmail(dto.getEmail());
        manager.setPassword(passwordEncoder.encode(dto.getPassword()));

        Manager saved = repo.save(manager);
        return convertToDTO(saved);
    }

    public void deleteManager(Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new RuntimeException("Manager does not exist");
        }
        repo.deleteById(id);
    }

    private ManagerResponseDTO convertToDTO(Manager manager) {
        return new ManagerResponseDTO(manager.getId(), manager.getFullName(), manager.getEmail());
    }
}