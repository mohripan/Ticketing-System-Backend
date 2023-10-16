package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.repository.AttachmentRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private final Path root = Paths.get("C:\\Users\\mohri\\OneDrive\\Documents\\file_test");

    public Attachment save(MultipartFile file, Integer ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            Path filePath = root.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);

            Attachment attachment = new Attachment();
            attachment.setTicket(ticket);
            attachment.setAttachmentPath(filePath.toString());
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setUploadDate(LocalDateTime.now());

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<Attachment> loadAllByTicket(Integer ticketId) {
        return attachmentRepository.findAll().stream()
                .filter(attachment -> attachment.getTicket().getTicketID().equals(ticketId))
                .collect(Collectors.toList());
    }

    public void delete(Integer attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        try {
            Files.delete(Paths.get(attachment.getAttachmentPath()));
            attachmentRepository.deleteById(attachmentId);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }

    public void deleteAllByTicket(Integer ticketId) {
        List<Attachment> attachments = attachmentRepository.findAll().stream()
                .filter(attachment -> attachment.getTicket().getTicketID().equals(ticketId))
                .collect(Collectors.toList());

        for (Attachment attachment : attachments) {
            try {
                Files.delete(Paths.get(attachment.getAttachmentPath()));
                attachmentRepository.delete(attachment);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
            }
        }
    }

    public Optional<Attachment> findById(Integer id) {
        return attachmentRepository.findById(id);
    }
}
