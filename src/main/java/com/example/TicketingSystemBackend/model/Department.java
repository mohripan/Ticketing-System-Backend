package com.example.TicketingSystemBackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {
    public Department(Long departmentID, String departmentName) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
    }

    public Long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="department_id")
    private Long departmentID;

    @Column(name="department_name")
    private String departmentName;
}
