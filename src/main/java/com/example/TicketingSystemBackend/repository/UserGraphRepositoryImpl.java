package com.example.TicketingSystemBackend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserGraphRepositoryImpl implements UserGraphRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> executeQuery(String query, Integer userID, LocalDateTime startDate, LocalDateTime endDate, Optional<String> severityOpt) {
        Query nativeQuery = entityManager.createNativeQuery(query)
                .setParameter("userID", userID)
                .setParameter("start_date", startDate)
                .setParameter("end_date", endDate);

        if (severityOpt.isPresent()) {
            nativeQuery.setParameter("severity_name", severityOpt.get());
        }

        return nativeQuery.getResultList();
    }
}
