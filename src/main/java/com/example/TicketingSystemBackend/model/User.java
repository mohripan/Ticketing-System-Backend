package com.example.TicketingSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userID;

    @Column(unique=true, name = "email")
    private String email;

    @Column(name="encrypted_password")
    private String encryptedPassword;

    @Column(name="user_name")
    private String userName;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    @JsonBackReference(value = "user-department")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    @JsonBackReference(value = "user-role")
    private Role role;

    public User() {
    }

    public User(Integer userID, String email, String encryptedPassword, String userName, String name, Department department, Role role) {
        this.userID = userID;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.userName = userName;
        this.name = name;
        this.department = department;
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}