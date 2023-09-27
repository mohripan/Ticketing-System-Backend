package com.example.TicketingSystemBackend.dto;

public class DepartmentSingleViewDTO {
    private Integer departmentID;
    private String departmentName;

    public DepartmentSingleViewDTO() {
    }

    public DepartmentSingleViewDTO(Integer departmentID, String departmentName) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
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
}
