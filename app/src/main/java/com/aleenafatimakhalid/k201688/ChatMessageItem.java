package com.aleenafatimakhalid.k201688;

public class ChatMessageItem {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;

    private String senderId;
    private String recipientId;
    private String message;
    private String timestamp;
    private int messageType;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    private String mediaUrl;

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    private String imageURl;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private String messageId;

    public ChatMessageItem() {
        // Default constructor required for Firebase
    }

    public ChatMessageItem(String senderId, String recipientId, String message, String timestamp, int messageType, String messageId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = timestamp;
        this.messageType = messageType;
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

  //  public void setMessageType(String messageType) {this.messageType = Integer.parseInt(messageType);}
}
