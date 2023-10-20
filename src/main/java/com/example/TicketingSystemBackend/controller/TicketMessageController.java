package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.ChatMessageDTO;
import com.example.TicketingSystemBackend.model.Customer;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.CustomerRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.EmailService;
import com.example.TicketingSystemBackend.service.TicketReplyService;
import com.example.TicketingSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
public class TicketMessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketReplyService ticketReplyService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    @MessageMapping("/user/{ticketId}/sendMessage")
    public void sendMessage(@Payload TicketReply ticketReply,
                            @DestinationVariable Integer ticketId,
                            @Header("simpSessionId") String sessionId,
                            Principal principal) {

        try {
            // Validate ticket
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            if (!ticketOpt.isPresent()) {
                sendErrorMessage(sessionId, "Ticket not found");
                return;
            }
            Ticket ticket = ticketOpt.get();
            if ("CLOSED".equals(ticket.getTicketStatus())) {
                sendErrorMessage(sessionId, "Cannot reply to a closed ticket.");
                return;
            }

            ticket.setTicketStatus("IN_PROGRESS");
            ticketRepository.save(ticket);

            // Determine sender type and validate
            User authenticatedUser = null;
            Customer authenticatedCustomer = null;

            if (principal instanceof UsernamePasswordAuthenticationToken) {
                authenticatedUser = (User) ((Authentication) principal).getPrincipal();
            } else {
                String email = principal.getName();
                authenticatedCustomer = customerRepository.findByEmail(email);
                if (authenticatedCustomer == null) {
                    sendErrorMessage(sessionId, "Customer not found");
                    return;
                }
            }

            // Update ticket reply with sender details
            ticketReply.setTicket(ticket);
            ticketReply.setUser(authenticatedUser);
            ticketReply.setCustomer(authenticatedCustomer);
            TicketReply savedReply = ticketReplyService.saveReply(ticketReply);

            // Send message to ticket-specific topic
            messagingTemplate.convertAndSend(
                    "/topic/tickets/" + ticket.getTicketID(),
                    savedReply
            );

            // Notify involved parties
            if (authenticatedUser != null) {
                // Send a confirmation message back to the user
                sendUserConfirmation(sessionId, "Message sent successfully");
                sendEmailNotification(ticket, ticketId, false);
            } else {
                // Send email notification to the user
                sendEmailNotification(ticket, ticketId, true);
            }

        } catch (Exception e) {
            sendErrorMessage(sessionId, "An error occurred: " + e.getMessage());
        }
    }


    private void sendErrorMessage(String sessionId, String message) {
        System.out.println("Sending error message. SessionId: " + sessionId + ", Message: " + message);
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", message);
    }

    private void sendUserConfirmation(String sessionId, String message) {
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/confirmations", message);
    }

    private void sendEmailNotification(Ticket ticket, Integer ticketId, boolean isCustomer) {
        String recipientEmail;
        String subject;
        String messageBody;

        if (isCustomer) {
            recipientEmail = ticket.getAssignedTo().getEmail();
            subject = "Ticket #" + ticket.getTicketNumber() + " Has Been Updated";
            messageBody = "Hello " + ticket.getAssignedTo().getName() + ",\n\n" +
                    "Ticket #" + ticket.getTicketNumber() + " has received a new reply from the customer. Please check it in our system.\n\n" +
                    "Regards,\n" +
                    "Support Team";
        } else {
            recipientEmail = ticket.getCustomer().getEmail();
            subject = "Your Ticket #" + ticket.getTicketNumber() + " Has Been Replied To";
            messageBody = "Dear " + ticket.getCustomer().getName() + ",\n\n" +
                    "Your ticket has received a new reply. Please check it in our system.\n\n" +
                    "Regards,\n" +
                    "Support Team";
        }

        emailService.sendSimpleMessage(recipientEmail, subject, messageBody);
    }

    @MessageMapping("/chat/{ticketId}")
    @SendTo("/topic/tickets/{ticketId}")
    public TicketReply forwardMessage(@DestinationVariable String ticketId, TicketReply ticketReply) {
        // Additional logic for processing the message if needed
        return ticketReply;
    }
}
