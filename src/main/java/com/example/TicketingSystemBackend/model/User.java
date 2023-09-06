package com.example.TicketingSystemBackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long userID;

    @Column(unique=true)
    private String email;

    @Column(name="encryptedPassword")
    private String encryptedPassword;

    @Column(name="userName")
    private String userName;

    @Column(name="name")
    private String name;

    @Column(name="departmentID")
    private Long departmentID;  // Assuming Department is another table

    @Enumerated(EnumType.STRING)
    private UserRole role;  // Enum

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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

    public Long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public User() {
    }
}

enum UserRole {
    STAFF, MANAGER
}