package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.*;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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

    @Autowired
    private EntityManager entityManager;


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

        if (UserRole.fromRoleId(user.getRole().getRoleId()) == UserRole.MANAGER) {
            return ticketRepository.findById(ticketId);
        }

        return Optional.empty();
    }

    public boolean closeTicket(Integer ticketId, User user) {
        Optional<Ticket> ticketOpt = ticketRepository.findByAssignedToAndTicketID(user, ticketId);
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

    public List<Ticket> getTicketsByAssignedToAndDepartmentAndFilters(User user, Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity, String status, String ticketNumber) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM Ticket t JOIN t.ticketTag tt JOIN tt.department d JOIN t.ticketSeverity ts WHERE t.assignedTo = :user AND d.departmentID = :departmentID");

        if (startDate != null) {
            queryStr.append(" AND t.createdDate >= :startDate");
        }

        if (endDate != null) {
            queryStr.append(" AND t.createdDate <= :endDate");
        }

        if (severity != null) {
            queryStr.append(" AND ts.severityName = :severity");
        }

        if (status != null) {
            queryStr.append(" AND t.ticketStatus = :status");
        }

        if (ticketNumber != null && !ticketNumber.trim().isEmpty()) {
            queryStr.append(" AND t.ticketNumber LIKE :ticketNumber");
        }

        Query query = entityManager.createQuery(queryStr.toString());
        query.setParameter("user", user);
        query.setParameter("departmentID", departmentID);

        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }

        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        if (severity != null) {
            query.setParameter("severity", severity);
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        if (ticketNumber != null && !ticketNumber.trim().isEmpty()) {
            query.setParameter("ticketNumber", "%" + ticketNumber + "%");
        }


        return query.getResultList();
    }

    public List<Ticket> getAllTicketsForManagerWithFilters(LocalDateTime startDate, LocalDateTime endDate, String severity, String status, String assignedUser, String ticketNumber) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM Ticket t");
        if (severity != null || status != null) {
            queryStr.append(" JOIN t.ticketSeverity ts");
        }
        if (assignedUser != null) {
            queryStr.append(" JOIN t.assignedTo u");
        }

        queryStr.append(" WHERE 1=1"); // A dummy condition to simplify the appending of other conditions

        if (startDate != null) {
            queryStr.append(" AND t.createdDate >= :startDate");
        }

        if (endDate != null) {
            queryStr.append(" AND t.createdDate < :endDate");
        }

        if (severity != null) {
            queryStr.append(" AND ts.severityName = :severity");
        }

        if (status != null) {
            queryStr.append(" AND t.ticketStatus = :status");
        }

        if (assignedUser != null) {
            queryStr.append(" AND u.userName = :assignedUser");
        }

        if (ticketNumber != null && !ticketNumber.trim().isEmpty()) {
            queryStr.append(" AND t.ticketNumber LIKE :ticketNumber");
        }

        Query query = entityManager.createQuery(queryStr.toString());

        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }

        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        if (severity != null) {
            query.setParameter("severity", severity);
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        if (assignedUser != null) {
            query.setParameter("assignedUser", assignedUser);
        }

        if (ticketNumber != null && !ticketNumber.trim().isEmpty()) {
            query.setParameter("ticketNumber", "%" + ticketNumber + "%");
        }

        return query.getResultList();
    }

}
