package com.example.TicketingSystemBackend.dto;

import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateTicketDTO {

    private Integer ticketID;

    private Integer userID;

    private String ticketNumber;

    private LocalDateTime createdDate;

    private String ticketContent;

    private String ticketStatus;

    private Integer assignedTo;

    private Integer ticketTagID;

    private Integer severityID;

    private Integer customerID;

    public CreateTicketDTO() {
    }

    public CreateTicketDTO(Integer ticketID, Integer userID, String ticketNumber, LocalDateTime createdDate, String ticketContent, String ticketStatus, Integer assignedTo, Integer ticketTagID, Integer severityID, Integer customerID) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
        this.assignedTo = assignedTo;
        this.ticketTagID = ticketTagID;
        this.severityID = severityID;
        this.customerID = customerID;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getTicketTagID() {
        return ticketTagID;
    }

    public void setTicketTagID(Integer ticketTagID) {
        this.ticketTagID = ticketTagID;
    }

    public Integer getSeverityID() {
        return severityID;
    }

    public void setSeverityID(Integer severityID) {
        this.severityID = severityID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }
}
