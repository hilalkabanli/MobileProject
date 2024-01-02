package com.example.ceyda.friendlypaws;

public class ForumMessage {
    private String messageText;
    private String senderMail;

    public ForumMessage() {
    }

    public ForumMessage(String messageText,String senderMail) {
        this.messageText = messageText;
        this.senderMail = senderMail;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getSenderMail(){
        return senderMail;
    }
}
