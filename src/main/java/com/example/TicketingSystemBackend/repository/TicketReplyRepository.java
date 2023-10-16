package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketReplyRepository extends JpaRepository<TicketReply, Integer> {
    List<TicketReply> findByTicket(Ticket ticket);

    List<TicketReply> findByTicketOrderByReplyDateDesc(Ticket ticket);
}
