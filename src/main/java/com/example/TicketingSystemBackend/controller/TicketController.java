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
}