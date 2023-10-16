package com.example.TicketingSystemBackend.dto;

public class EmailPayloadDTO {
    private String sender;
    private String subject;
    private String bodyPlain;

    public EmailPayloadDTO() {
    }

    public EmailPayloadDTO(String sender, String subject, String bodyPlain) {
        this.sender = sender;
        this.subject = subject;
        this.bodyPlain = bodyPlain;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyPlain() {
        return bodyPlain;
    }

    public void setBodyPlain(String bodyPlain) {
        this.bodyPlain = bodyPlain;
    }
}
