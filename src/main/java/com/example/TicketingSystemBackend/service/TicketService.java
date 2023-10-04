package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.CreateTicketDTO;
import com.example.TicketingSystemBackend.model.*;
import com.example.TicketingSystemBackend.repository.*;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TicketService {

    private static final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TicketTagRepository ticketTagRepository;

    @Autowired
    private TicketSeverityRepository ticketSeverityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Ticket createTicket(CreateTicketDTO dto) {
        Ticket ticket = new Ticket();

        TicketTag ticketTag = ticketTagRepository.findById(dto.getTicketTagID())
                .orElseThrow(() -> new RuntimeException("Ticket tag not found"));

        Department department = ticketTag.getDepartment();

        User assignedTo = userRepository.findTopByDepartmentAndRole_RoleId(department, 1)
                .orElseThrow(() -> new RuntimeException("No manager found for the department"));

        TicketSeverity ticketSeverity = ticketSeverityRepository.findById(dto.getSeverityID())
                .orElseThrow(() -> new RuntimeException("Ticket severity not found"));

        Customer customer = customerRepository.findById(dto.getCustomerID())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ticket.setTicketNumber(generateUniqueTicketNumber());
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setTicketContent(dto.getTicketContent());
        ticket.setTicketStatus("PENDING");
        ticket.setAssignedTo(assignedTo);
        ticket.setTicketTag(ticketTag);
        ticket.setTicketSeverity(ticketSeverity);
        ticket.setCustomer(customer);

        return ticketRepository.save(ticket);
    }

    private String generateUniqueTicketNumber() {
        int randomNumber = secureRandom.nextInt(1_000_000);  // generates a random number between 0 (inclusive) and 1,000,000 (exclusive)
        return "TICK" + String.format("%06d", randomNumber) + System.currentTimeMillis();
    }


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
        Optional<Ticket> ticket = ticketRepository.findByAssignedToAndTicketID(user, ticketId);
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
        StringBuilder queryStr = new StringBuilder("SELECT t FROM Ticket t JOIN t.ticketTag tt JOIN tt.department d JOIN t.ticketSeverity ts WHERE t.assignedTo = :assignedTo AND d.departmentID = :departmentID");

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
        query.setParameter("assignedTo", user);
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

    public Long getTicketsCountByTimeframe(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.countTicketsByTimeframe(start, end);
    }

    public Object[] getResponseTimeMetrics(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.findResponseTimeMetrics(start, end);
    }

    public Long getTicketsCountByDepartmentAndTimeframe(Integer departmentID, LocalDateTime start, LocalDateTime end) {
        return ticketRepository.countTicketsByDepartmentAndTimeframe(departmentID, start, end);
    }

    public List<Object[]> getTicketsBySeverityAndDepartment(Integer departmentID, LocalDateTime start, LocalDateTime end) {
        return ticketRepository.countTicketsBySeverityAndDepartment(departmentID, start, end);
    }

    public List<Object[]> getDailyTicketTrafficByDepartment(Integer departmentID, LocalDateTime start, LocalDateTime end) {
        return ticketRepository.dailyTicketTrafficByDepartment(departmentID, start, end);
    }

    public Long countTicketsByUserAndTimeframe(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.countTicketsByUserAndTimeframe(userID, startDate, endDate);
    }

    public Object[] findUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findUserResponseTimeMetrics(userID, startDate, endDate);
    }

    public Object[] countUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.countUserResponseTimeMetrics(userID, startDate, endDate);
    }

    public List<Object[]> findDailyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findDailyResponseTimeMetricsForUser(userID, startDate, endDate);
    }

    public List<Object[]> findWeeklyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findWeeklyResponseTimeMetricsForUser(userID, startDate, endDate);
    }

    public List<Object[]> findMonthlyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findMonthlyResponseTimeMetricsForUser(userID, startDate, endDate);
    }

    public List<Object[]> averageDailyResponseTimeByDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.averageDailyResponseTimeByDepartment(departmentID, startDate, endDate);
    }

    public List<Object[]> findResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
    }

    public List<Object[]> findDailyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findDailyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
    }

    public List<Object[]> findWeeklyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findWeeklyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
    }

    public List<Object[]> findMonthlyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findMonthlyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
    }
}
