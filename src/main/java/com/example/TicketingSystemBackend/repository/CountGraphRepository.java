package com.example.TicketingSystemBackend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CountGraphRepository {
    Long executeQuery(String query, Optional<Integer> departmentIDOpt, Optional<Integer> userIDOpt, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt);
}
