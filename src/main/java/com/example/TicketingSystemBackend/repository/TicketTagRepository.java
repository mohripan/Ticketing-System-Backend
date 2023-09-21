package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.TicketTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTagRepository extends JpaRepository<TicketTag, Integer> {
    List<TicketTag> findAll();

    List<TicketTag> findByDepartment_DepartmentID(Integer departmentID);
}
