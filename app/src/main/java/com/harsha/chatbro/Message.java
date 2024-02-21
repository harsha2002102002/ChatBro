package com.harsha.chatbro;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageText;
private Object timestamp;
    public Message() {
     }

    public Message(String messageId, String senderId, String receiverId, String messageText,Object timestamp ) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
     }



    // Getters and setters for properties
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public Object getTimestamp() {
        return  timestamp;
    }
}
