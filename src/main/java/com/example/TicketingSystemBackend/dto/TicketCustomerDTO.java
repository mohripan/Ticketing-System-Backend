package com.example.TicketingSystemBackend.dto;

import com.example.TicketingSystemBackend.model.TicketReply;

import java.time.LocalDateTime;
import java.util.List;

public class TicketCustomerDTO {
    private Integer ticketID;
    private String ticketNumber;
    private LocalDateTime createdDate;
    private String ticketContent;
    private String ticketStatus;
    private List<TicketReply> ticketReplies;

    public TicketCustomerDTO() {
    }

    public TicketCustomerDTO(Integer ticketID, String ticketNumber, LocalDateTime createdDate, String ticketContent, String ticketStatus, List<TicketReply> ticketReplies) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
        this.ticketReplies = ticketReplies;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
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

    public List<TicketReply> getTicketReplies() {
        return ticketReplies;
    }

    public void setTicketReplies(List<TicketReply> ticketReplies) {
        this.ticketReplies = ticketReplies;
    }
}
