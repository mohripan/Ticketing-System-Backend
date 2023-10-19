package com.example.TicketingSystemBackend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DepartmentGraphRepository {
    List<Object[]> executeQuery(String query, Integer departmentID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt);
}
