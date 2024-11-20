package com.service;

import com.dto.CustomerRequest;
import com.dto.LoginRequest;
import com.entity.Customer;
import com.helper.JwtUtil;
import com.mapper.CustomerMapper;
import com.repo.CustomerRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public String createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.password()));
        customerRepo.save(customer);
        return "Customer created successfully";
    }

    public String login(LoginRequest request) {
        Optional<Customer> customerOptional = customerRepo.findByEmail(request.email());

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (passwordEncoder.matches(request.password(), customer.getPassword())) {//raw string, encrypt. pass
                return jwtUtil.generateToken(customer.getEmail());
            }
        }
        throw new RuntimeException("Invalid credentials");
    }
//    public String updateCustomer(Long id, CustomerRequest request) {
//        Customer customer = customerRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
//        customer.setFirstName(request.firstName());
//        customer.setLastName(request.lastName());
//        customer.setEmail(request.email());
//        customer.setPassword(passwordEncoder.encode(request.password())); // Ensure password is encoded
//        customer.setAddress(request.address());
//        customer.setCity(request.city());
//        customer.setPincode(request.pincode());
//        customerRepo.save(customer);
//        return "Customer updated successfully";
//    }

    public String updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        // Update only the provided fields
        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.password() != null) {
            customer.setPassword(passwordEncoder.encode(request.password())); // Encode if password is updated
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
        if (request.city() != null) {
            customer.setCity(request.city());
        }
        if (request.pincode() != null) {
            customer.setPincode(request.pincode());
        }

        // Save the updated customer to the database
        customerRepo.save(customer);
        return "Customer updated successfully";
    }


    public String deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        customerRepo.delete(customer);
        return "Customer deleted successfully";
    }
}