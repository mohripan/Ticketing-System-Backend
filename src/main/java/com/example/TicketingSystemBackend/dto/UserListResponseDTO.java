package com.example.TicketingSystemBackend.dto;

public class UserListResponseDTO {
    private Integer userID;
    private String email;
    private String userName;
    private String name;

    public UserListResponseDTO() {
    }

    public UserListResponseDTO(Integer userID, String email, String userName, String name) {
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.name = name;
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
}
