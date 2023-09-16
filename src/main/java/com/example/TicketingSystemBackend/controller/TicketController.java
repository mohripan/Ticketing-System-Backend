package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.User;
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

import java.util.List;

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
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('READ_TICKETS')")
    @GetMapping("/user/myTickets")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        User authenticatedUser = authenticationService.getAuthenticatedUser(token);
        Integer departmentID = authenticationService.getDepartmentFromToken(token);
        List<Ticket> tickets = ticketService.getTicketsByAssignedToAndDepartment(authenticatedUser, departmentID);
        return ResponseEntity.ok(tickets);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/manager/allTicketsForManager")
    public ResponseEntity<List<Ticket>> getAllTicketsForManager() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @PreAuthorize("hasAuthority('CLOSE_TICKETS')")
    @PutMapping("/user/close/{ticketId}")
    public ResponseEntity<Void> closeTicket(@PathVariable Integer ticketId,
                                            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isClosed = ticketService.closeTicket(ticketId, authenticatedUser);

        if (!isClosed) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
