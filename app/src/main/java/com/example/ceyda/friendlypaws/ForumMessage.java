package com.example.ceyda.friendlypaws;

public class ForumMessage {
    private String messageText;
    private String senderId;
    private String senderEmail;

    public ForumMessage() {
    }

    public ForumMessage(String messageText, String senderId) {
        this.messageText = messageText;
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
}
