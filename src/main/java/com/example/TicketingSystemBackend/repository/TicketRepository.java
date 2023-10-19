package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.TicketSeverity;
import com.example.TicketingSystemBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByAssignedToAndTicketID(User assignedTo, Integer ticketID);
    List<Ticket> findByAssignedToAndTicketTag_Department_DepartmentID(User assignedTo, Integer departmentID);
    List<Ticket> findAll();

    List<Ticket> findByCustomer_CustomerID(Integer customerID);
}
