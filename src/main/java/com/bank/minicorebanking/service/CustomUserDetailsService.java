package com.bank.minicorebanking.service;

import com.bank.minicorebanking.entity.Admin;
import com.bank.minicorebanking.entity.Manager;
import com.bank.minicorebanking.entity.User;
import com.bank.minicorebanking.repository.AdminRepository;
import com.bank.minicorebanking.repository.ManagerRepository;
import com.bank.minicorebanking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final ManagerRepository managerRepo;

    public CustomUserDetailsService(UserRepository userRepo, AdminRepository adminRepo, ManagerRepository managerRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.managerRepo = managerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepo.findByEmail(usernameOrEmail);
        if (admin.isPresent()) {
            return admin.get();
        }

        Optional<Manager> manager = managerRepo.findByEmail(usernameOrEmail);
        if (manager.isPresent()) {
            return manager.get();
        }

        Optional<User> user = userRepo.findByUsername(usernameOrEmail);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UsernameNotFoundException("User not found");
    }
}