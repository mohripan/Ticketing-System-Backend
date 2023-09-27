package com.example.TicketingSystemBackend.repository;

import com.example.TicketingSystemBackend.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}
