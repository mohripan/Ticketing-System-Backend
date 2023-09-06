package com.example.TicketingSystemBackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userID;

    @Column(unique=true, name = "email")
    private String email;

    @Column(name="encrypted_password")
    private String encryptedPassword;

    @Column(name="user_name")
    private String userName;

    @Column(name="name")
    private String name;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @Column(name="role")
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public User(Long userID, String email, String encryptedPassword, String userName, String name, Department department, String role) {
        this.userID = userID;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.userName = userName;
        this.name = name;
        this.department = department;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}