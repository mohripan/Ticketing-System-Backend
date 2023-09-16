package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketSeverity;
import com.example.TicketingSystemBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByUserAndTicketID(User user, Integer ticketID);
    List<Ticket> findByAssignedToAndTicketTag_Department_DepartmentID(User assignedTo, Integer departmentID);
    List<Ticket> findAll();
}
