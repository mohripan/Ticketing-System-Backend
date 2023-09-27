package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.repository.AttachmentRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Attachment saveAttachment(MultipartFile file, Integer ticketId) throws IOException {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        Attachment attachment = new Attachment();

        // Inside the project's folder:
        // String directory = "test_file";

        // Outside the project's folder:
        String directory = "C:\\Users\\mohri\\OneDrive\\Documents\\file_test";

        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String originalFileName = file.getOriginalFilename();
        Path filePath = path.resolve(originalFileName);
        file.transferTo(filePath.toFile());

        attachment.setTicket(ticket);
        attachment.setAttachmentPath(filePath.toString());
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUploadDate(LocalDateTime.now());

        return attachmentRepository.save(attachment);
    }

    public Optional<Attachment> findById(Integer id) {
        return attachmentRepository.findById(id);
    }
}
