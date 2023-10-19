package com.example.TicketingSystemBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.TicketingSystemBackend.exception.UnsupportedFileTypeException;
import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.repository.AttachmentRepository;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private Cloudinary cloudinary;

    // private final Path root = Paths.get("src/main/resources/static");

    public Attachment save(MultipartFile file, Integer ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            Map<String, String> uploadResult;
            String resourceType = determineResourceType(file.getContentType());

            // Check the determined resource type and upload accordingly
            if ("raw".equals(resourceType)) {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "raw"
                ));
            } else {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            }

            Attachment attachment = new Attachment();
            attachment.setTicket(ticket);
            attachment.setCloudinaryPublicId(uploadResult.get("public_id").toString());
            attachment.setAttachmentPath(uploadResult.get("url").toString());
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setUploadDate(LocalDateTime.now());

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    private String determineResourceType(String contentType) throws UnsupportedFileTypeException {
        List<String> imageTypes = Arrays.asList("image/jpeg", "image/jpg");
        List<String> documentTypes = Arrays.asList("application/pdf", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (imageTypes.contains(contentType)) {
            return "image";
        } else if (documentTypes.contains(contentType)) {
            return "document";
        } else {
            throw new UnsupportedFileTypeException("File type is not supported");
        }
    }

    public Resource load(String cloudinaryPublicId) {
        try {
            Attachment attachment = attachmentRepository.findByCloudinaryPublicId(cloudinaryPublicId)
                    .orElseThrow(() -> new RuntimeException("Attachment not found"));

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(attachment.getAttachmentPath(), byte[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return new ByteArrayResource(response.getBody());
            } else {
                throw new RuntimeException("Could not fetch the file from Cloudinary");
            }
        } catch (Exception e) {
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

        // Delete file from Cloudinary using publicId
        try {
            cloudinary.uploader().destroy(attachment.getCloudinaryPublicId(), ObjectUtils.emptyMap());
            attachmentRepository.deleteById(attachmentId);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }

    public void deleteAllByTicket(Integer ticketId) {
        List<Attachment> attachments = attachmentRepository.findAll().stream()
                .filter(attachment -> attachment.getTicket().getTicketID().equals(ticketId))
                .collect(Collectors.toList());

        for (Attachment attachment : attachments) {
            try {
                cloudinary.uploader().destroy(attachment.getCloudinaryPublicId(), ObjectUtils.emptyMap());
                attachmentRepository.delete(attachment);
            } catch (Exception e) {
                throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
            }
        }
    }

    public Optional<Attachment> findById(Integer id) {
        return attachmentRepository.findById(id);
    }
}
