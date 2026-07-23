package com.bank.minicorebanking.controller;

import com.bank.minicorebanking.dto.ManagerRequestDTO;
import com.bank.minicorebanking.dto.ManagerResponseDTO;
import com.bank.minicorebanking.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService service;

    public ManagerController(ManagerService service) {
        this.service = service;
    }

    @PostMapping
    public ManagerResponseDTO createManager(@Valid @RequestBody ManagerRequestDTO dto) {
        return service.createManager(dto);
    }

    @GetMapping
    public List<ManagerResponseDTO> getAllManagers() {
        return service.getAllManagers();
    }

    @GetMapping("/{id}")
    public ManagerResponseDTO getManagerById(@PathVariable Long id) {
        return service.getManagerById(id);
    }

    @PutMapping("/{id}")
    public ManagerResponseDTO updateManager(@PathVariable Long id, @Valid @RequestBody ManagerRequestDTO dto) {
        return service.updateManager(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        service.deleteManager(id);
    }
}