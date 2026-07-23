package com.bank.minicorebanking.controller;

import com.bank.minicorebanking.dto.AdminRequestDTO;
import com.bank.minicorebanking.dto.AdminResponseDTO;
import com.bank.minicorebanking.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping
    public AdminResponseDTO createAdmin(@Valid @RequestBody AdminRequestDTO dto) {
        return service.createAdmin(dto);
    }

    @GetMapping
    public List<AdminResponseDTO> getAllAdmins() {
        return service.getAllAdmins();
    }

    @GetMapping("/{id}")
    public AdminResponseDTO getAdminById(@PathVariable Long id) {
        return service.getAdminById(id);
    }

    @PutMapping("/{id}")
    public AdminResponseDTO updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminRequestDTO dto) {
        return service.updateAdmin(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        service.deleteAdmin(id);
    }
}