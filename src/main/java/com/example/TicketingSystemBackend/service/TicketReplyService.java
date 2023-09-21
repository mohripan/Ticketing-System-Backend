package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.model.UserRole;
import com.example.TicketingSystemBackend.repository.TicketReplyRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketReplyService {
    @Autowired
    private TicketReplyRepository ticketReplyRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<TicketReply> getRepliesByTicket(Ticket ticket) {
        return ticketReplyRepository.findByTicket(ticket);
    }

    public TicketReply saveReply(TicketReply ticketReply) {
        return ticketReplyRepository.save(ticketReply);
    }
}
