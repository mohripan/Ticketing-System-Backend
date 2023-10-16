package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.TicketCustomerDTO;
import com.example.TicketingSystemBackend.model.Customer;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import com.example.TicketingSystemBackend.repository.CustomerRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Customer saveOrUpdateCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findByPhoneNumberAndEmailAndName(customer.getPhoneNumber(), customer.getEmail(), customer.getName());

        if (!existingCustomer.isPresent()) {
            return customerRepository.save(customer);
        }
        return existingCustomer.get();
    }

    public Optional<Customer> findByPhoneNumberAndEmailAndName(String phoneNumber, String email, String name) {
        return customerRepository.findByPhoneNumberAndEmailAndName(phoneNumber, email, name);
    }

    public List<TicketCustomerDTO> getTicketsByCustomerID(Integer customerID) {
        List<Ticket> tickets = ticketRepository.findByCustomer_CustomerID(customerID);
        return tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TicketCustomerDTO convertToDTO(Ticket ticket) {
        List<TicketReply> ticketReplyDTOs = ticket.getTicketReplies().stream()
                .map(reply -> new TicketReply(reply.getReplyID(), reply.getTicket(), reply.getReplyContent(), reply.getUser(), reply.getCustomer(), reply.getReplyDate()))
                .collect(Collectors.toList());

        return new TicketCustomerDTO(ticket.getTicketID(),
                ticket.getTicketNumber(),
                ticket.getCreatedDate(),
                ticket.getTicketContent(),
                ticket.getTicketStatus(),
                ticketReplyDTOs);
    }
}
