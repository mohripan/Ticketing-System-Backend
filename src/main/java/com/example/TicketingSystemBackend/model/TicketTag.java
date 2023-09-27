package com.example.TicketingSystemBackend.model;

import com.example.TicketingSystemBackend.dto.TicketTagDTO;
import com.example.TicketingSystemBackend.dto.TicketTagShowDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    @JsonBackReference(value = "tag-department")
    private Department department;

    @OneToMany(mappedBy = "ticketTag", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "ticket-tag")
    private List<Ticket> tickets;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    @JsonBackReference(value = "tag-user")
    private User user;

    public TicketTag() {
    }

    public TicketTag(Integer ticketTagID, String tagName, String description, LocalDateTime createdDate, Department department, List<Ticket> tickets, User user) {
        this.ticketTagID = ticketTagID;
        this.tagName = tagName;
        this.description = description;
        this.createdDate = createdDate;
        this.department = department;
        this.tickets = tickets;
        this.user = user;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TicketTagDTO toDTO() {
        TicketTagDTO dto = new TicketTagDTO();
        dto.setTicketTagID(this.getTicketTagID());
        dto.setTagName(this.getTagName());
        dto.setDescription(this.getDescription());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setDepartmentID(this.getDepartment().getDepartmentID());
        dto.setCreatedBy(this.getUser().getUserID());
        return dto;
    }

    public TicketTagShowDTO showToDTO() {
        TicketTagShowDTO dto = new TicketTagShowDTO();
        dto.setTagName(this.getTagName());
        dto.setDescription(this.getDescription());
        dto.setDepartmentID(this.getDepartment().getDepartmentID());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setDepartmentName(this.getDepartment().getDepartmentName());
        dto.setCreatedByUsername(this.getUser().getUserName());
        return dto;
    }
}
