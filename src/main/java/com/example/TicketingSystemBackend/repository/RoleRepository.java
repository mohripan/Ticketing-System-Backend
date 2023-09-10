package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
