package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.CreateTicketDTO;
import com.example.TicketingSystemBackend.model.Customer;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.service.CustomerService;
import com.example.TicketingSystemBackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save-or-update")
    public ResponseEntity<Customer> saveOrUpdateCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.saveOrUpdateCustomer(customer);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/check-existence")
    public ResponseEntity<Customer> checkExistingCustomer(@RequestParam String phoneNumber, @RequestParam String email, @RequestParam String name) {
        Optional<Customer> customer = customerService.findCustomerByPhoneNumberAndEmail(phoneNumber, email, name);

        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save-ticket")
    public ResponseEntity<Ticket> createTicket(@RequestBody CreateTicketDTO ticketDTO) {
        Ticket createdTicket = ticketService.createTicket(ticketDTO);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }
}
