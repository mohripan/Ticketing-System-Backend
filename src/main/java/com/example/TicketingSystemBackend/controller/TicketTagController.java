package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.TicketTagDTO;
import com.example.TicketingSystemBackend.dto.TicketTagShowDTO;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketTag;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.TicketTagService;
import com.example.TicketingSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ticket-tag")
public class TicketTagController {
    @Autowired
    private TicketTagService ticketTagService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('READ_TICKET_TAGS')")
    @GetMapping("/list")
    public ResponseEntity<List<TicketTagShowDTO>> listTicketTags(@RequestParam(required = false) Integer departmentID) {
        return ResponseEntity.ok(ticketTagService.getAllTicketTags(Optional.ofNullable(departmentID)));
    }

    @PreAuthorize("hasAuthority('MANAGE_TICKET_TAGS')")
    @PostMapping("/add")
    public ResponseEntity<TicketTagDTO> createTicketTag(@RequestBody TicketTagDTO ticketTagDTO,
                                                        @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);


        TicketTag ticketTag = ticketTagService.createTicketTag(ticketTagDTO, authenticatedUser);
        return ResponseEntity.ok(ticketTag.toDTO());
    }

    @PreAuthorize("hasAuthority('MANAGE_TICKET_TAGS')")
    @PutMapping("/edit/{ticketTagID}")
    public ResponseEntity<TicketTagDTO> editTicketTag(@PathVariable Integer ticketTagID,
                                                      @RequestBody TicketTagDTO ticketTagDTO,
                                                      @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtUtil.extractUsername(token);
        User authenticatedUser = userService.getUserByEmail(userEmail);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TicketTag updatedTicketTag = ticketTagService.editTicketTag(ticketTagID, ticketTagDTO, authenticatedUser);
        return ResponseEntity.ok(updatedTicketTag.toDTO());
    }

    @PreAuthorize("hasAuthority('MANAGE_TICKET_TAGS')")
    @DeleteMapping("/delete/{ticketTagID}")
    public ResponseEntity<Void> deleteTicketTag(@PathVariable Integer ticketTagID) {
        ticketTagService.deleteTicketTag(ticketTagID);
        return ResponseEntity.noContent().build();
    }
}
