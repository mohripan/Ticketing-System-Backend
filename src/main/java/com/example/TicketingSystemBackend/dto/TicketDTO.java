package com.example.TicketingSystemBackend.dto;

import com.example.TicketingSystemBackend.model.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDTO {
    private Integer useID;
    private String userName;
    private String ticketNumber;
    private LocalDateTime createdDate;
    private List<Attachment> attachments = new ArrayList<>();
    private String ticketContent;
    private String ticketStatus;

    public TicketDTO() {
    }

    public TicketDTO(Integer useID, String userName, String ticketNumber, LocalDateTime createdDate, List<Attachment> attachments, String ticketContent, String ticketStatus) {
        this.useID = useID;
        this.userName = userName;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.attachments = attachments;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
    }

    public Integer getUseID() {
        return useID;
    }

    public void setUseID(Integer useID) {
        this.useID = useID;
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
