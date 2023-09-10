package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Integer id) {
        return departmentRepository.findById(id);
    }

    public Department updateDepartment(Integer id, Department department) {
        if(departmentRepository.existsById(id)) {
            department.setDepartmentID(id);
            return departmentRepository.save(department);
        }

        return null;
    }

    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }
}
