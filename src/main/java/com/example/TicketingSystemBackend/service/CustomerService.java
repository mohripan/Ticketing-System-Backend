package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Customer;
import com.example.TicketingSystemBackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveOrUpdateCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findByPhoneNumberAndEmailAndName(customer.getPhoneNumber(), customer.getEmail(), customer.getName());

        if (!existingCustomer.isPresent()) {
            return customerRepository.save(customer);
        }
        return existingCustomer.get();
    }

    public Optional<Customer> findCustomerByPhoneNumberAndEmail(String phoneNumber, String email, String name) {
        return customerRepository.findByPhoneNumberAndEmailAndName(phoneNumber, email, name);
    }
}
