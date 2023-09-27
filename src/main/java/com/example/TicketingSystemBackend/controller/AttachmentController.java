package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload/{ticketId}")
    public ResponseEntity<Attachment> uploadAttachment(@PathVariable Integer ticketId,
                                                       @RequestParam("file") MultipartFile file) {
        try {
            Attachment attachment = attachmentService.saveAttachment(file, ticketId);
            return new ResponseEntity<>(attachment, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<Attachment> getAttachment(@PathVariable Integer attachmentId) {
        Optional<Attachment> attachment = attachmentService.findById(attachmentId);
        return attachment.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
