package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.*;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

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
