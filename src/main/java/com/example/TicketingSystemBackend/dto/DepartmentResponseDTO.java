package com.example.TicketingSystemBackend.dto;

import java.util.List;

public class DepartmentResponseDTO {
    private Integer departmentID;
    private String departmentName;
    private List<UserListResponseDTO> users;

    public DepartmentResponseDTO() {
    }

    public DepartmentResponseDTO(Integer departmentID, String departmentName, List<UserListResponseDTO> users) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.users = users;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<UserListResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserListResponseDTO> users) {
        this.users = users;
    }
}
