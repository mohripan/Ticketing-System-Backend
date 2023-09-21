package com.example.TicketingSystemBackend.dto;

import java.time.LocalDateTime;

public class TicketTagDTO {
    private Integer ticketTagID;
    private String tagName;
    private String description;
    private LocalDateTime createdDate;
    private Integer departmentID;
    private Integer createdBy;

    public TicketTagDTO() {
    }

    public TicketTagDTO(Integer ticketTagID, String tagName, String description, LocalDateTime createdDate, Integer departmentID, Integer createdBy) {
        this.ticketTagID = ticketTagID;
        this.tagName = tagName;
        this.description = description;
        this.createdDate = createdDate;
        this.departmentID = departmentID;
        this.createdBy = createdBy;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}
