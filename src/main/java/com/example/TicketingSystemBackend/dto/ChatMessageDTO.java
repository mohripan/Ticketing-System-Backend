package com.example.TicketingSystemBackend.dto;

public class ChatMessageDTO {
    private Integer senderId;
    private String content;
    private String timestamp;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(Integer senderId, String content, String timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
