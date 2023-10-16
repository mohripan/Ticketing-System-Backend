package com.example.TicketingSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ticket_replies")
public class TicketReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private int replyID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    @JsonBackReference(value = "ticket_reply-ticket")
    private Ticket ticket;

    @Column(name = "reply_content", nullable = false)
    private String replyContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "ticket_reply-user")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonBackReference(value = "ticket_reply-customer")
    private Customer customer;

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    public TicketReply() {
    }

    public TicketReply(int replyID, Ticket ticket, String replyContent, User user, Customer customer, LocalDateTime replyDate) {
        this.replyID = replyID;
        this.ticket = ticket;
        this.replyContent = replyContent;
        this.user = user;
        this.customer = customer;
        this.replyDate = replyDate;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonIgnore // Ignore during serialization
    public User getUser() {
        return user;
    }

    @JsonIgnore // Ignore during serialization
    public Customer getCustomer() {
        return customer;
    }

    public Integer getUserId() {
        return user != null ? user.getUserID() : null;
    }

    public Integer getCustomerId() {
        return customer != null ? customer.getCustomerID() : null;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    @PrePersist
    protected void onCreate() {
        this.replyDate = LocalDateTime.now();
    }
}
