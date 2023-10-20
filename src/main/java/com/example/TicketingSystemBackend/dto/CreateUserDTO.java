package com.example.TicketingSystemBackend.dto;

public class CreateUserDTO {
    private String email;
    private String encryptedPassword;
    private String userName;
    private String name;
    private Integer departmentID;
    private Integer roleId;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String email, String encryptedPassword, String userName, String name, Integer departmentID, Integer roleId) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.userName = userName;
        this.name = name;
        this.departmentID = departmentID;
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
