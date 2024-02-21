package com.harsha.chatbro;

public class ItemModel {
    private String name;
    private String photoUrl;
    private String uid;

private String lastMessage;
private long timestamp;
    public ItemModel(String name, String photoUrl, String uid  ) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.uid = uid;

    }

    public ItemModel(String lastMessage, Object timestamp) {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
