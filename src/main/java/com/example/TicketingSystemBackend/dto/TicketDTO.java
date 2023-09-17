package com.example.TicketingSystemBackend.dto;

import java.time.LocalDateTime;

public class TicketDTO {
    private String userName;
    private String ticketNumber;
    private LocalDateTime createdDate;
    private String ticketAttachmentPath;
    private String ticketContent;
    private String ticketStatus;

    public TicketDTO() {
    }

    public TicketDTO(String userName, String ticketNumber, LocalDateTime createdDate, String ticketAttachmentPath, String ticketContent, String ticketStatus) {
        this.userName = userName;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.ticketAttachmentPath = ticketAttachmentPath;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
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

    public String getTicketAttachmentPath() {
        return ticketAttachmentPath;
    }

    public void setTicketAttachmentPath(String ticketAttachmentPath) {
        this.ticketAttachmentPath = ticketAttachmentPath;
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
