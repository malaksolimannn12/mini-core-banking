package com.bank.minicorebanking.service;

import com.bank.minicorebanking.dto.CustomerRequestDTO;
import com.bank.minicorebanking.dto.CustomerResponseDTO;
import com.bank.minicorebanking.entity.Account;
import com.bank.minicorebanking.entity.Customer;
import com.bank.minicorebanking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        Optional<Customer> existing = repo.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Customer with this email already exists");
        } else {
            Customer customer = new Customer();
            customer.setFullName(dto.getFullName());
            customer.setEmail(dto.getEmail());
            customer.setPhoneNumber(dto.getPhoneNumber());

            Customer saved = repo.save(customer);
            return convertToDTO(saved);
        }
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
//The same code using a normal for loop
//
//This code does exactly the same thing:
    //public List<CustomerResponseDTO> getAllCustomers() {
//
//    List<Customer> customers = repo.findAll();
//
//    List<CustomerResponseDTO> result = new ArrayList<>();
//
//    for(Customer customer : customers){
//        result.add(convertToDTO(customer));
//    }
//
//    return result;
//}
    //findAll() → Get all customers.
//stream() → Go through them one by one.
//map(convertToDTO) → Convert each Customer into a CustomerResponseDTO.
//collect(toList()) → Put all the DTOs into a new list and return it.

    public CustomerResponseDTO getCustomerById(Long id) {
        Optional<Customer> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Customer does not exist");
        } else {
            return convertToDTO(existing.get());
        }
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto) {
        Optional<Customer> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Customer does not exist");
        } else {
            Customer customerToUpdate = existing.get();
            customerToUpdate.setFullName(dto.getFullName());
            customerToUpdate.setEmail(dto.getEmail());
            customerToUpdate.setPhoneNumber(dto.getPhoneNumber());

            Customer saved = repo.save(customerToUpdate);
            return convertToDTO(saved);
        }
    }

    public void deleteCustomer(Long id) {
        Optional<Customer> existing = repo.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Customer does not exist");
        } else {
            repo.deleteById(id);
        }
    }

    private CustomerResponseDTO convertToDTO(Customer customer) {
        List<String> accountNumbers = customer.getAccounts() == null
                ? new ArrayList<>()
                : customer.getAccounts().stream()
                  .map(Account::getAccountNumber)
                  .collect(Collectors.toList());

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                accountNumbers
        );
    }
}