package com.example.TicketingSystemBackend.dto;

import java.time.LocalDateTime;

public class TicketTagShowDTO {

    private Integer tagID;
    private String tagName;
    private String description;
    private Integer departmentID;
    private String departmentName;
    private LocalDateTime createdDate;
    private String createdByUsername;

    public TicketTagShowDTO() {
    }

    public TicketTagShowDTO(Integer tagID, String tagName, String description, Integer departmentID, String departmentName, LocalDateTime createdDate, String createdByUsername) {
        this.tagID = tagID;
        this.tagName = tagName;
        this.description = description;
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.createdDate = createdDate;
        this.createdByUsername = createdByUsername;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
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

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
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
