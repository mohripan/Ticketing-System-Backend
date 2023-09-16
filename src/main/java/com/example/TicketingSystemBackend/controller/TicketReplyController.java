package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.TicketReplyService;
import com.example.TicketingSystemBackend.service.TicketService;
import com.example.TicketingSystemBackend.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/replies")
public class TicketReplyController {
    @Autowired
    private TicketReplyService ticketReplyService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasAuthority('REPLY_TICKETS')")
    @PostMapping("/user/{ticketId}")
    public ResponseEntity<TicketReply> postReply(@PathVariable int ticketId,
                                                 @RequestBody TicketReply newReply,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);

        if(authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Ticket> ticketOpt = ticketService.findTicketById(ticketId, authenticatedUser);

        if (!ticketOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        newReply.setTicket(ticketOpt.get());
        newReply.setUser(authenticatedUser);
        TicketReply savedReply = ticketReplyService.saveReply(newReply);
        return ResponseEntity.ok(savedReply);
    }

//    @GetMapping("/ticket/{ticketId}")
//    public ResponseEntity<List<TicketReply>> getRepliesForTicket(@PathVariable int ticketId) {
//        Ticket ticket = ticketService.findTicketById(ticketId);
//        if (ticket == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<TicketReply> replies = ticketReplyService.getRepliesForTicket(ticket);
//        return ResponseEntity.ok(replies);
//    }
}
