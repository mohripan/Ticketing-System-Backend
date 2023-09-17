package com.example.TicketingSystemBackend.model;

import com.example.TicketingSystemBackend.dto.TicketDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "ticket-user")
    private User user;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "ticket_attachment_path")
    private String ticketAttachmentPath;

    @Column(name = "ticket_content")
    private String ticketContent;

    @Column(name = "ticket_status")
    private String ticketStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    @JsonBackReference(value = "assigned-user")
    private User assignedTo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ticket_tag_id")
    @JsonBackReference(value = "ticket-tag")
    private TicketTag ticketTag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="severity_id")
    @JsonBackReference(value = "ticket-severity")
    private TicketSeverity ticketSeverity;

    public Ticket() {
    }

    public Ticket(Integer ticketID, User user, String ticketNumber, LocalDateTime createdDate, String ticketAttachmentPath, String ticketContent, String ticketStatus, User assignedTo, TicketTag ticketTag, TicketSeverity ticketSeverity) {
        this.ticketID = ticketID;
        this.user = user;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.ticketAttachmentPath = ticketAttachmentPath;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
        this.assignedTo = assignedTo;
        this.ticketTag = ticketTag;
        this.ticketSeverity = ticketSeverity;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TicketTag getTicketTag() {
        return ticketTag;
    }

    public void setTicketTag(TicketTag ticketTag) {
        this.ticketTag = ticketTag;
    }

    public TicketSeverity getTicketSeverity() {
        return ticketSeverity;
    }

    public void setTicketSeverity(TicketSeverity ticketSeverity) {
        this.ticketSeverity = ticketSeverity;
    }

    public TicketDTO toDTO() {
        TicketDTO dto = new TicketDTO();
        dto.setUserName(this.getUser().getUserName());
        dto.setTicketNumber(this.getTicketNumber());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setTicketAttachmentPath(this.getTicketAttachmentPath());
        dto.setTicketContent(this.getTicketContent());
        dto.setTicketStatus(this.getTicketStatus());
        return dto;
    }
}
