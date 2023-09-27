package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findTopByDepartmentAndRole_RoleId(Department department, Integer roleID);
    List<User> findByDepartmentDepartmentIDAndRoleRoleId(Integer departmentID, Integer roleId);
}
