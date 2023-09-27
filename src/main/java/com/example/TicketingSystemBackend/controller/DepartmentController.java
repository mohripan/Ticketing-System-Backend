package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT')")
    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PreAuthorize("hasAuthority('VIEW_DEPARTMENT')")
    @GetMapping("/view")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PreAuthorize("hasAuthority('VIEW_DEPARTMENT')")
    @GetMapping("/view/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Integer id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Integer id, Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        if(updatedDepartment != null) {
            return ResponseEntity.ok(updatedDepartment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
