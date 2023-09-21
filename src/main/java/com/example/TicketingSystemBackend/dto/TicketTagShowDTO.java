package com.example.TicketingSystemBackend.dto;

import java.time.LocalDateTime;

public class TicketTagShowDTO {
    private String tagName;
    private String description;
    private String departmentName;
    private LocalDateTime createdDate;
    private String createdByUsername;

    public TicketTagShowDTO() {
    }

    public TicketTagShowDTO(String tagName, String description, String departmentName, LocalDateTime createdDate, String createdByUsername) {
        this.tagName = tagName;
        this.description = description;
        this.departmentName = departmentName;
        this.createdDate = createdDate;
        this.createdByUsername = createdByUsername;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
}
