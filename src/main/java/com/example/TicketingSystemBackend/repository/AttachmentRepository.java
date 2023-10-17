package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Optional<Attachment> findByCloudinaryPublicId(String cloudinaryPublicId);
}
