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
    private final TicketRepository ticketRepository;
    private final DepartmentGraphRepository departmentGraphRepository;
    private final UserGraphRepository userGraphRepository;
    private final CountGraphRepository countGraphRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, DepartmentGraphRepository departmentGraphRepository, UserGraphRepository userGraphRepository, CountGraphRepository countGraphRepository) {
        this.ticketRepository = ticketRepository;
        this.departmentGraphRepository = departmentGraphRepository;
        this.userGraphRepository = userGraphRepository;
        this.countGraphRepository = countGraphRepository;
    }

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

    public Long countTickets(Optional<Integer> departmentIDOpt, Optional<Integer> userIDOpt, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT COUNT(t.ticket_id) FROM tickets t ");
        sb.append("JOIN ticket_tags tt ON t.ticket_tag_id = tt.ticket_tag_id ");
        sb.append("JOIN departments d ON tt.department_id = d.department_id ");
        sb.append("JOIN users u ON t.assigned_to = u.user_id ");
        if (severityOpt.isPresent()) {
            sb.append("JOIN ticket_severities ts ON t.severity_id = ts.severity_id ");
            sb.append("AND ts.severity_name = :severity_name ");
        }
        sb.append("WHERE t.created_date BETWEEN :start_date AND :end_date ");

        if (departmentIDOpt.isPresent()) {
            sb.append("AND d.department_id = :departmentID ");
        }

        if (userIDOpt.isPresent()) {
            sb.append("AND u.user_id = :userID ");
        }

        return countGraphRepository.executeQuery(sb.toString(), departmentIDOpt, userIDOpt, startDate, endDate, severityOpt);
    }


    public List<Object[]> getResponseTimeMetricsDepartment(Optional<String> granularityOpt, Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        StringBuilder sb = new StringBuilder();

        String timeFunction = ""; // Default if granularity is not provided
        if (granularityOpt.isPresent()) {
            switch (granularityOpt.get()) {
                case "daily":
                    timeFunction = "DATE(sub.created_date), ";
                    break;
                case "weekly":
                    timeFunction = "WEEK(sub.created_date), ";
                    break;
                case "monthly":
                    timeFunction = "MONTH(sub.created_date), ";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid granularity");
            }
        }

        sb.append("SELECT ");
        sb.append(timeFunction);
        sb.append("AVG(sub.reply_diff), ");
        sb.append("MIN(sub.reply_diff), ");
        sb.append("MAX(sub.reply_diff), ");
        sb.append("COUNT(sub.reply_id) ");
        sb.append("FROM (");
        sb.append("SELECT tr.reply_id, tr.reply_date, t.created_date, ");
        sb.append("IFNULL(TIMESTAMPDIFF(SECOND, COALESCE(LAG(tr.reply_date) OVER (PARTITION BY t.ticket_id ORDER BY tr.reply_date), t.created_date), tr.reply_date), 0) AS reply_diff ");
        sb.append("FROM tickets t ");
        sb.append("JOIN ticket_replies tr ON t.ticket_id = tr.ticket_id ");
        sb.append("JOIN ticket_tags tt ON t.ticket_tag_id = tt.ticket_tag_id ");
        sb.append("JOIN ticket_severities ttt ON t.severity_id = ttt.severity_id ");
        sb.append("WHERE tt.department_id = :departmentID AND ");
        sb.append("t.created_date BETWEEN :start_date AND :end_date ");
        if(severityOpt.isPresent()) {
            sb.append("AND ttt.severity_name = :severity_name ");
        }
        sb.append(") AS sub ");

        if (!timeFunction.isEmpty()) {
            sb.append("GROUP BY ");
            sb.append(timeFunction.substring(0, timeFunction.length() - 2));
        }

        return departmentGraphRepository.executeQuery(sb.toString(), departmentID, startDate, endDate, severityOpt);
    }

    public List<Object[]> getResponseTimeMetricsUser(Optional<String> granularityOpt, Integer userID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        StringBuilder sb = new StringBuilder();

        String timeFunction = determineTimeFunction(granularityOpt); // Extracted to a helper function for clarity and reuse

        sb.append("SELECT sub.user_id, sub.user_name, ");
        sb.append(timeFunction);
        sb.append("AVG(sub.reply_diff), ");
        sb.append("MIN(sub.reply_diff), ");
        sb.append("MAX(sub.reply_diff), ");
        sb.append("COUNT(sub.reply_id) ");
        sb.append("FROM (");
        sb.append("SELECT tr.reply_id, tr.reply_date, t.created_date, u.user_id, u.user_name, ");
        sb.append("IFNULL(TIMESTAMPDIFF(SECOND, COALESCE(LAG(tr.reply_date) OVER (PARTITION BY t.ticket_id ORDER BY tr.reply_date), t.created_date), tr.reply_date), 0) AS reply_diff ");
        sb.append("FROM tickets t ");
        sb.append("JOIN ticket_replies tr ON t.ticket_id = tr.ticket_id ");
        sb.append("JOIN users u ON tr.user_id = u.user_id ");
        sb.append("JOIN ticket_severities ttt ON t.severity_id = ttt.severity_id ");
        sb.append("WHERE u.user_id = :userID AND ");
        sb.append("t.created_date BETWEEN :start_date AND :end_date ");
        if(severityOpt.isPresent()) {
            sb.append("AND ttt.severity_name = :severity_name ");
        }
        sb.append(") AS sub ");

        sb.append("GROUP BY sub.user_id, ");
        sb.append(timeFunction.substring(0, timeFunction.length() - 2));

        return userGraphRepository.executeQuery(sb.toString(), userID, startDate, endDate, severityOpt);
    }


    public List<Object[]> getResponseTimeMetricsDepartmentPerUser(Optional<String> granularityOpt, Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        StringBuilder sb = new StringBuilder();

        String timeFunction = determineTimeFunction(granularityOpt); // Using the helper function for time granularity

        sb.append("SELECT sub.user_id, sub.user_name, ");
        sb.append(timeFunction);
        sb.append("AVG(sub.reply_diff), ");
        sb.append("MIN(sub.reply_diff), ");
        sb.append("MAX(sub.reply_diff), ");
        sb.append("COUNT(sub.reply_id) ");
        sb.append("FROM (");
        sb.append("SELECT tr.reply_id, tr.reply_date, t.created_date, tr.user_id, u.user_name, ");
        sb.append("IFNULL(TIMESTAMPDIFF(SECOND, COALESCE(LAG(tr.reply_date) OVER (PARTITION BY t.ticket_id ORDER BY tr.reply_date), t.created_date), tr.reply_date), 0) AS reply_diff ");
        sb.append("FROM tickets t ");
        sb.append("JOIN ticket_replies tr ON t.ticket_id = tr.ticket_id ");
        sb.append("JOIN users u ON tr.user_id = u.user_id ");
        sb.append("JOIN ticket_tags tt ON t.ticket_tag_id = tt.ticket_tag_id ");
        sb.append("JOIN ticket_severities ttt ON t.severity_id = ttt.severity_id ");
        sb.append("WHERE tt.department_id = :departmentID AND ");
        sb.append("t.created_date BETWEEN :start_date AND :end_date ");
        if(severityOpt.isPresent()) {
            sb.append("AND ttt.severity_name = :severity_name ");
        }
        sb.append(") AS sub ");

        sb.append("GROUP BY sub.user_id, ");
        sb.append(timeFunction.substring(0, timeFunction.length() - 2));

        return departmentGraphRepository.executeQuery(sb.toString(), departmentID, startDate, endDate, severityOpt);
    }

    // Helper function to determine time granularity
    private String determineTimeFunction(Optional<String> granularityOpt) {
        if (!granularityOpt.isPresent()) {
            return "";
        }
        switch (granularityOpt.get()) {
            case "daily":
                return "DATE(sub.created_date), ";
            case "weekly":
                return "WEEK(sub.created_date), ";
            case "monthly":
                return "MONTH(sub.created_date), ";
            default:
                throw new IllegalArgumentException("Invalid granularity");
        }
    }

//    public Long getTicketsCountByTimeframe(LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.countTicketsByTimeframe(start, end);
//    }
//
//    public Object[] getResponseTimeMetrics(LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.findResponseTimeMetrics(start, end);
//    }
//
//    public Long getTicketsCountByDepartmentAndTimeframe(Integer departmentID, LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.countTicketsByDepartmentAndTimeframe(departmentID, start, end);
//    }
//
//    public List<Object[]> getTicketsBySeverityAndDepartment(Integer departmentID, LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.countTicketsBySeverityAndDepartment(departmentID, start, end);
//    }
//
//    public List<Object[]> getDailyTicketTrafficByDepartment(Integer departmentID, LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.dailyTicketTrafficByDepartment(departmentID, start, end);
//    }
//
//    public Long countTicketsByUserAndTimeframe(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.countTicketsByUserAndTimeframe(userID, startDate, endDate);
//    }
//
//    public Object[] findUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findUserResponseTimeMetrics(userID, startDate, endDate);
//    }
//
//    public Object[] countUserResponseTimeMetrics(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.countUserResponseTimeMetrics(userID, startDate, endDate);
//    }
//
//    public List<Object[]> findDailyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findDailyResponseTimeMetricsForUser(userID, startDate, endDate);
//    }
//
//    public List<Object[]> findWeeklyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findWeeklyResponseTimeMetricsForUser(userID, startDate, endDate);
//    }
//
//    public List<Object[]> findMonthlyResponseTimeMetricsForUser(Integer userID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findMonthlyResponseTimeMetricsForUser(userID, startDate, endDate);
//    }
//
//    public List<Object[]> averageDailyResponseTimeByDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.averageDailyResponseTimeByDepartment(departmentID, startDate, endDate);
//    }
//
//    public List<Object[]> findResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
//    }
//
//    public List<Object[]> findDailyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findDailyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
//    }
//
//    public List<Object[]> findWeeklyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findWeeklyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
//    }
//
//    public List<Object[]> findMonthlyResponseTimeMetricsPerUserInDepartment(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate) {
//        return ticketRepository.findMonthlyResponseTimeMetricsPerUserInDepartment(departmentID, startDate, endDate);
//    }
//
//    public Long countTicketsByTimeframeAndSeverity(LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.countTicketsByTimeframeAndSeverity(startDate, endDate, severity);
//    }
//
//    public Long countTicketsByDepartmentTimeframeAndSeverity(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.countTicketsByDepartmentTimeframeAndSeverity(departmentID, startDate, endDate, severity);
//    }
//
//    public Long countTicketsByUserTimeframeAndSeverity(Integer userID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.countTicketsByUserTimeframeAndSeverity(userID, startDate, endDate, severity);
//    }
//
//    public Object[] findUserResponseTimeMetricsBySeverity(Integer userID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findUserResponseTimeMetricsBySeverity(userID, startDate, endDate, severity);
//    }
//
//    public Object[] findResponseTimeMetricsPerDepartmentBySeverity(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findResponseTimeMetricsPerDepartmentBySeverity(departmentID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findDailyResponseTimeMetricsForUserBySeverity(Integer userID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findDailyResponseTimeMetricsForUserBySeverity(userID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findWeeklyResponseTimeMetricsForUserBySeverity(Integer userID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findWeeklyResponseTimeMetricsForUserBySeverity(userID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findMonthlyResponseTimeMetricsForUserBySeverity(Integer userID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findMonthlyResponseTimeMetricsForUserBySeverity(userID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findDailyResponseTimeMetricsPerDepartmentBySeverity(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findDailyResponseTimeMetricsPerDepartmentBySeverity(departmentID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findWeeklyResponseTimeMetricsPerDepartmentBySeverity(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findWeeklyResponseTimeMetricsPerDepartmentBySeverity(departmentID, startDate, endDate, severity);
//    }
//
//    public List<Object[]> findMonthlyResponseTimeMetricsPerDepartmentBySeverity(Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, String severity) {
//        return ticketRepository.findMonthlyResponseTimeMetricsPerDepartmentBySeverity(departmentID, startDate, endDate, severity);
//    }
}
