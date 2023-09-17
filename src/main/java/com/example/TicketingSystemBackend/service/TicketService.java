package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.*;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket assignTicketToUser(Integer ticketId, User partialAssignedTo) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (!ticketOpt.isPresent()) {
            throw new NoSuchElementException("Ticket not found with ID: " + ticketId);
        }

        Ticket ticket = ticketOpt.get();

        // Check if the assignedTo user exists in the database
        Optional<User> userOpt = userRepository.findById(partialAssignedTo.getUserID());
        if (!userOpt.isPresent()) {
            throw new NoSuchElementException("User not found with ID: " + partialAssignedTo.getUserID());
        }

        User assignedTo = userOpt.get();

        if (!ticket.getTicketTag().getDepartment().getDepartmentID().equals(assignedTo.getDepartment().getDepartmentID())) {
            throw new IllegalArgumentException("The user does not belong to the correct department for this ticket.");
        }

        ticket.setAssignedTo(assignedTo);
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> findTicketById(Integer ticketId, User user) {
        Optional<Ticket> ticket = ticketRepository.findByUserAndTicketID(user, ticketId);
        if (ticket.isPresent()) {
            return ticket;
        }

        // If not, and the user is a MANAGER, fetch the ticket.
        if (UserRole.fromRoleId(user.getRole().getRoleId()) == UserRole.MANAGER) {
            return ticketRepository.findById(ticketId);
        }

        return Optional.empty();
    }

    public boolean closeTicket(Integer ticketId, User user) {
        Optional<Ticket> ticketOpt = findTicketById(ticketId, user);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setTicketStatus("CLOSED");
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByAssignedToAndDepartment(User assignedTo, Integer departmentID) {
        return ticketRepository.findByAssignedToAndTicketTag_Department_DepartmentID(assignedTo, departmentID);
    }
}
