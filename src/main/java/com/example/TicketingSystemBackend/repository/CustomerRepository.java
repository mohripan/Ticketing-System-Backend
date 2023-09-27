package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByPhoneNumberAndEmailAndName(String phoneNumber, String email, String name);
}
