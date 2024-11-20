package com.mapper;


import org.springframework.stereotype.Service;

import com.dto.CustomerRequest;
import com.dto.CustomerResponse;
import com.entity.Customer;


@Service
public class CustomerMapper {
    public Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .build();
    }
    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }
}
