package com.example.TicketingSystemBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ticket_tags")
public class TicketTag {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ticket_tag_id")
    private Integer ticketTagID;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    @JsonBackReference(value = "tag-department")
    private Department department;

    @OneToMany(mappedBy = "ticketTag", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "ticket-tag")
    private List<Ticket> tickets;

    public TicketTag() {
    }

    public TicketTag(Integer ticketTagID, String tagName, String description, Department department, List<Ticket> tickets) {
        this.ticketTagID = ticketTagID;
        this.tagName = tagName;
        this.description = description;
        this.department = department;
        this.tickets = tickets;
    }

    public Integer getTicketTagID() {
        return ticketTagID;
    }

    public void setTicketTagID(Integer ticketTagID) {
        this.ticketTagID = ticketTagID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
