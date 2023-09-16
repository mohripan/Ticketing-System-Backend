package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketReplyRepository extends JpaRepository<TicketReply, Integer> {
    List<TicketReply> findByTicket(Ticket ticket);
}
