package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.message.ResponseMessage;
import com.example.TicketingSystemBackend.model.Attachment;
import com.example.TicketingSystemBackend.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload/{ticketId}")
    public ResponseEntity<Attachment> uploadAttachment(@PathVariable Integer ticketId,
                                                       @RequestParam("file") MultipartFile file) {
        try {
            Attachment attachment = attachmentService.save(file, ticketId);
            return new ResponseEntity<>(attachment, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload/multiple/{ticketId}")
    public ResponseEntity<List<Attachment>> uploadMultipleAttachments(@PathVariable Integer ticketId,
                                                                      @RequestParam("files") MultipartFile[] files) {
        try {
            List<Attachment> attachments = new ArrayList<>();
            for (MultipartFile file : files) {
                Attachment attachment = attachmentService.save(file, ticketId);
                attachments.add(attachment);
            }
            return new ResponseEntity<>(attachments, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = attachmentService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/list/{ticketId}")
    public ResponseEntity<List<Attachment>> listAttachments(@PathVariable Integer ticketId) {
        List<Attachment> attachments = attachmentService.loadAllByTicket(ticketId);
        return new ResponseEntity<>(attachments, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Integer attachmentId) {
        try {
            attachmentService.delete(attachmentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/all/{ticketId}")
    public ResponseEntity<?> deleteAllAttachments(@PathVariable Integer ticketId) {
        try {
            attachmentService.deleteAllByTicket(ticketId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
