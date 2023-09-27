package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.EmailService;
import com.example.TicketingSystemBackend.service.TicketReplyService;
import com.example.TicketingSystemBackend.service.TicketService;
import com.example.TicketingSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/replies")
public class TicketReplyController {
    @Autowired
    private TicketReplyService ticketReplyService;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasAuthority('REPLY_TICKETS')")
    @PostMapping("/user/{ticketId}")
    public ResponseEntity<?> postReply(@PathVariable int ticketId,
                                                 @RequestBody TicketReply newReply,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);

        if(authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Ticket> ticketOpt = ticketRepository.findByAssignedToAndTicketID(authenticatedUser, ticketId);

        if (!ticketOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = ticketOpt.get();
        if ("CLOSED".equals(ticket.getTicketStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot reply to a closed ticket.");
        }

        ticket.setTicketStatus("IN_PROGRESS");
        ticketRepository.save(ticket);

        newReply.setTicket(ticketOpt.get());
        newReply.setUser(authenticatedUser);
        TicketReply savedReply = ticketReplyService.saveReply(newReply);

        // Send email notification to the customer
        String customerEmail = ticketOpt.get().getCustomer().getEmail();
        String subject = "Your Ticket #" + ticketId + " Has Been Replied To";
        String message = "Dear " + ticketOpt.get().getCustomer().getName() + ",\n\n" +
                "Your ticket has received a new reply. Please check it in our system.\n\n" +
                "Regards,\n" +
                "Support Team";
        emailService.sendSimpleMessage(customerEmail, subject, message);

        return ResponseEntity.ok(savedReply);
    }
}
