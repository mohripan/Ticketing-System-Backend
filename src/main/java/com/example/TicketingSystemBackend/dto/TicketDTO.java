package com.example.TicketingSystemBackend.dto;

import com.example.TicketingSystemBackend.model.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDTO {

    private Integer ticketID;
    private Integer userID;
    private String userName;
    private Integer customerID;
    private String customerName;
    private String ticketNumber;
    private LocalDateTime createdDate;
    private List<Attachment> attachments = new ArrayList<>();
    private String ticketContent;
    private String ticketStatus;

    public TicketDTO() {
    }

    public TicketDTO(Integer ticketID, Integer userID, String userName, Integer customerID, String customerName, String ticketNumber, LocalDateTime createdDate, List<Attachment> attachments, String ticketContent, String ticketStatus) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.userName = userName;
        this.customerID = customerID;
        this.customerName = customerName;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.attachments = attachments;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getTicketContent() {
        return ticketContent;
    }

    public void setTicketContent(String ticketContent) {
        this.ticketContent = ticketContent;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
