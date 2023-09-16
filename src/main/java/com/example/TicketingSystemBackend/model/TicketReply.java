package com.example.TicketingSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    public TicketReply() {
    }

    public TicketReply(int replyID, Ticket ticket, String replyContent, User user, LocalDateTime replyDate) {
        this.replyID = replyID;
        this.ticket = ticket;
        this.replyContent = replyContent;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
