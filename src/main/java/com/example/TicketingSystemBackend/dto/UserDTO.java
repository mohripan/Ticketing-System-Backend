package com.example.TicketingSystemBackend.dto;

public class UserDTO {
    private Integer userID;
    private String email;
    private String userName;
    private String name;
    private Integer departmentID;
    private Integer roleID;

    public UserDTO() {
    }

    public UserDTO(Integer userID, String email, String userName, String name, Integer departmentID, Integer roleID) {
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.name = name;
        this.departmentID = departmentID;
        this.roleID = roleID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }
}
