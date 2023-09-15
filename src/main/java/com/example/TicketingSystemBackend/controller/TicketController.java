package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.DateRangeDTO;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketSeverity;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.TicketSeverityRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import com.example.TicketingSystemBackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/byUser/{userID}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Integer userID) {
        User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketService.getTicketsByUser(user));
    }

    @GetMapping("/byStatus/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping("/bySeverity/{severityId}")
    public ResponseEntity<List<Ticket>> getTicketsBySeverity(@PathVariable Integer severityId) {
        TicketSeverity severity = ticketSeverityRepository.findById(severityId).orElse(null);
        if (severity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketService.getTicketsBySeverity(severity));
    }

    @PostMapping("/byDateRange")
    public ResponseEntity<List<Ticket>> getTicketsByDateRange(@RequestBody DateRangeDTO dateRange) {
        return ResponseEntity.ok(ticketService.getTicketsByDateRange(dateRange.getStartDate(), dateRange.getEndDate()));
    }
}
