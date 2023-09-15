package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketSeverity;
import com.example.TicketingSystemBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUser(User user);
    List<Ticket> findByAssignedTo(Integer assignedTo);
    List<Ticket> findByTicketTagDepartment(Department department);
    List<Ticket> findByTicketStatus(String ticketStatus);
    List<Ticket> findByTicketSeverity(TicketSeverity ticketSeverity);
    List<Ticket> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
