package com.example.TicketingSystemBackend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class CountGraphRepositoryImpl implements CountGraphRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long executeQuery(String query, Optional<Integer> departmentIDOpt, Optional<Integer> userIDOpt, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("start_date", startDate)
                .setParameter("end_date", endDate);

        severityOpt.ifPresent(severity -> nativeQuery.setParameter("severity_name", severity));
        departmentIDOpt.ifPresent(departmentID -> nativeQuery.setParameter("departmentID", departmentID));
        userIDOpt.ifPresent(userID -> nativeQuery.setParameter("userID", userID));

        return ((Number) nativeQuery.getSingleResult()).longValue();
    }
}