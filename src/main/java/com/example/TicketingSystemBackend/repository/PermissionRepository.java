package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
