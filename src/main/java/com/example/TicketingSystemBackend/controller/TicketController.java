package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.TicketDTO;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import com.example.TicketingSystemBackend.repository.TicketSeverityRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.AuthenticationService;
import com.example.TicketingSystemBackend.service.TicketService;
import com.example.TicketingSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    public static class TicketFilter {
        public LocalDateTime startDate;
        public LocalDateTime endDate;
        public String severity;
        public String status;
        public String assignedUser;
        public String ticketNumber;
    }
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketSeverityRepository ticketSeverityRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @PreAuthorize("hasAuthority('READ_TICKETS')")
    @GetMapping("/user/myTickets")
    public ResponseEntity<List<TicketDTO>> getTicketsByUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ticketNumber
    ) {
        token = token.replace("Bearer ", "");
        User authenticatedUser = authenticationService.getAuthenticatedUser(token);
        Integer departmentID = authenticationService.getDepartmentFromToken(token);
        List<Ticket> tickets = ticketService.getTicketsByAssignedToAndDepartmentAndFilters(authenticatedUser, departmentID, startDate, endDate, severity, status, ticketNumber);
        List<TicketDTO> ticketDTOs = tickets.stream().map(Ticket::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ticketDTOs);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/manager/allTicketsForManager")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForManager(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignedUser,
            @RequestParam(required = false) String ticketNumber
    ) {
        List<Ticket> tickets = ticketService.getAllTicketsForManagerWithFilters(startDate, endDate, severity, status, assignedUser, ticketNumber);
        List<TicketDTO> ticketDTOs = tickets.stream().map(Ticket::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ticketDTOs);
    }

//    @MessageMapping("/manager/filterTickets")
//    public void filterTickets(TicketFilter filter) {
//        List<Ticket> tickets = ticketService.getAllTicketsForManagerWithFilters(filter.startDate, filter.endDate, filter.severity, filter.status, filter.assignedUser, filter.ticketNumber);
//        List<TicketDTO> ticketDTOs = tickets.stream().map(Ticket::toDTO).collect(Collectors.toList());
//
//        simpMessagingTemplate.convertAndSend("/topic/tickets", ticketDTOs);
//    }

    @PreAuthorize("hasAuthority('CLOSE_TICKETS')")
    @PutMapping("/user/close/{ticketId}")
    public ResponseEntity<?> closeTicket(@PathVariable Integer ticketId,
                                            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Ticket> ticketOpt = ticketRepository.findByAssignedToAndTicketID(authenticatedUser, ticketId);

        Ticket ticket = ticketOpt.get();
        if ("CLOSED".equals(ticket.getTicketStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot close ticket that is already closed.");
        }

        boolean isClosed = ticketService.closeTicket(ticketId, authenticatedUser);

        if (!isClosed) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ASSIGN_TICKETS')")
    @PutMapping("/manager/assign/{ticketId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Integer ticketId,
                                               @RequestBody User assignedTo) {
        Ticket updatedTicket = ticketService.assignTicketToUser(ticketId, assignedTo);
        return ResponseEntity.ok(updatedTicket);
    }

    @PreAuthorize("hasAuthority('COUNT_TICKETS')")
    @GetMapping("/countTickets")
    public ResponseEntity<Long> getTicketCount(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Long count = ticketService.getTicketsCountByTimeframe(start, end);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/responseTimeMetrics")
    public ResponseEntity<Object[]> getResponseTimeMetrics(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Object[] metrics = ticketService.getResponseTimeMetrics(start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/countTicketsByDepartment/{departmentID}")
    public ResponseEntity<Long> getTicketsCountByDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Long count = ticketService.getTicketsCountByDepartmentAndTimeframe(departmentID, start, end);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/countTicketsByUser/{userID}")
    public ResponseEntity<Long> getTicketsByUserTimeframe(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Long count = ticketService.countTicketsByUserAndTimeframe(userID, start, end);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/userResponse/{userID}")
    public ResponseEntity<Object[]> findUserResponseTimeMetrics(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Object[] metrics = ticketService.findUserResponseTimeMetrics(userID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/countUserResponse/{userID}")
    public ResponseEntity<Object[]> countUserResponseTimeMetrics(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        Object[] metrics = ticketService.countUserResponseTimeMetrics(userID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/dailyUserResponse/{userID}")
    public ResponseEntity<List<Object[]>> findDailyResponseTimeMetricsForUser(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findDailyResponseTimeMetricsForUser(userID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/weeklyUserResponse/{userID}")
    public ResponseEntity<List<Object[]>> findWeeklyResponseTimeMetricsForUser(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findWeeklyResponseTimeMetricsForUser(userID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/monthlyUserResponse/{userID}")
    public ResponseEntity<List<Object[]>> findMonthlyResponseTimeMetricsForUser(@PathVariable Integer userID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findMonthlyResponseTimeMetricsForUser(userID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/dailyResponseDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> averageDailyResponseTimeByDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.averageDailyResponseTimeByDepartment(departmentID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/responsePerUserDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> findResponseTimeMetricsPerUserInDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findResponseTimeMetricsPerUserInDepartment(departmentID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/dailyResponsePerUserDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> findDailyResponseTimeMetricsPerUserInDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findDailyResponseTimeMetricsPerUserInDepartment(departmentID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/weeklyResponsePerUserDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> findWeeklyResponseTimeMetricsPerUserInDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findWeeklyResponseTimeMetricsPerUserInDepartment(departmentID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_METRICS')")
    @GetMapping("/monthlyResponsePerUserDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> findMonthlyResponseTimeMetricsPerUserInDepartment(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> metrics = ticketService.findMonthlyResponseTimeMetricsPerUserInDepartment(departmentID, start, end);
        return ResponseEntity.ok(metrics);
    }

    @PreAuthorize("hasAuthority('GET_SEVERITY_DISTRIBUTION')")
    @GetMapping("/ticketsBySeverity/{departmentID}")
    public ResponseEntity<List<Object[]>> getTicketsBySeverity(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> distribution = ticketService.getTicketsBySeverityAndDepartment(departmentID, start, end);
        return ResponseEntity.ok(distribution);
    }

    @PreAuthorize("hasAuthority('GET_DAILY_TRAFFIC')")
    @GetMapping("/dailyTrafficByDepartment/{departmentID}")
    public ResponseEntity<List<Object[]>> getDailyTicketTraffic(@PathVariable Integer departmentID, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<Object[]> traffic = ticketService.getDailyTicketTrafficByDepartment(departmentID, start, end);
        return ResponseEntity.ok(traffic);
    }
}