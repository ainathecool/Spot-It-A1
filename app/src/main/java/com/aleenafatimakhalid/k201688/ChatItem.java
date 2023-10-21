package com.aleenafatimakhalid.k201688;

public class ChatItem {
    private String chatName;
    private String lastMessage;
    private String avatarImageUrl; // Store the URL of the avatar image

    // Constructors, getters, and setters
    public ChatItem() {
        // Default constructor required for Firebase
    }
    public ChatItem(String chatName, String lastMessage, String avatarImageUrl) {
        this.chatName = chatName;
        this.lastMessage = lastMessage;
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }
}
