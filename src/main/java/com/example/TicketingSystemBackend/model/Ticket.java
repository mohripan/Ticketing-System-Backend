package com.example.TicketingSystemBackend.model;

import com.example.TicketingSystemBackend.dto.CreateTicketDTO;
import com.example.TicketingSystemBackend.dto.TicketDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketID;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonBackReference(value = "ticket-customer")
    private Customer customer;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "ticket_reply-ticket")
    private List<TicketReply> ticketReplies = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(Integer ticketID, String ticketNumber, LocalDateTime createdDate, List<Attachment> attachments, String ticketContent, String ticketStatus, User assignedTo, TicketTag ticketTag, TicketSeverity ticketSeverity, Customer customer, List<TicketReply> ticketReplies) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.createdDate = createdDate;
        this.attachments = attachments;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;
        this.assignedTo = assignedTo;
        this.ticketTag = ticketTag;
        this.ticketSeverity = ticketSeverity;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<TicketReply> getTicketReplies() {
        return ticketReplies;
    }

    public void setTicketReplies(List<TicketReply> ticketReplies) {
        this.ticketReplies = ticketReplies;
    }

    public TicketDTO toDTO() {
        TicketDTO dto = new TicketDTO();
        dto.setUseID(this.assignedTo != null ? this.assignedTo.getUserID() : null);
        dto.setUserName(this.assignedTo != null ? this.assignedTo.getUserName() : null);
        dto.setTicketNumber(this.getTicketNumber());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setAttachments(this.getAttachments());
        dto.setTicketContent(this.getTicketContent());
        dto.setTicketStatus(this.getTicketStatus());
        return dto;
    }

    public CreateTicketDTO createTicketDTO() {
        CreateTicketDTO dto = new CreateTicketDTO();
        dto.setTicketID(this.getTicketID());
        dto.setTicketNumber(this.getTicketNumber());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setTicketContent(this.getTicketContent());
        dto.setTicketStatus(this.getTicketStatus());
        dto.setAssignedTo(this.getAssignedTo().getUserID());
        dto.setTicketTagID(this.getTicketTag().getTicketTagID());
        dto.setSeverityID(this.getTicketSeverity().getSeverityID());
        dto.setCustomerID(this.getCustomer().getCustomerID());
        return dto;
    }
}
