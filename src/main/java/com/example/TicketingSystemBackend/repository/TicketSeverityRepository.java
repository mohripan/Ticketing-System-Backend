package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.TicketSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSeverityRepository extends JpaRepository<TicketSeverity, Integer> {
}
