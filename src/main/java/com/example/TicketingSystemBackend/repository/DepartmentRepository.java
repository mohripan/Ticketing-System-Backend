package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
