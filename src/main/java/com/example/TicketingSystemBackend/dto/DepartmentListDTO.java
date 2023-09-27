package com.example.TicketingSystemBackend.dto;

import com.example.TicketingSystemBackend.model.User;

import java.util.List;

public class DepartmentListDTO {
    private List<UserListResponseDTO> users;

    public DepartmentListDTO() {
    }

    public DepartmentListDTO(List<UserListResponseDTO> users) {
        this.users = users;
    }

    public List<UserListResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserListResponseDTO> users) {
        this.users = users;
    }
}
