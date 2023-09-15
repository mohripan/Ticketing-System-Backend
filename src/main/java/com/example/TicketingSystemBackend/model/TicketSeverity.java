package com.example.TicketingSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ticket_severities")
public class TicketSeverity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "severity_id")
    private Integer severityID;

    @Column(name = "severity_name")
    private String severityName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "ticketSeverity", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "ticket-severity")
    private List<Ticket> tickets;

    public TicketSeverity() {
    }

    public Integer getSeverityID() {
        return severityID;
    }

    public void setSeverityID(Integer severityID) {
        this.severityID = severityID;
    }

    public String getSeverityName() {
        return severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
