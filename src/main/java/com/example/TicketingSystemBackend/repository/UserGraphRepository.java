package com.example.TicketingSystemBackend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserGraphRepository {
    List<Object[]> executeQuery(String query, Integer userID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt);
}
