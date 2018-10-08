package com.example.android.mychat;

import java.sql.Timestamp;

public class Message {
    String userName, message,sharedImgUrl;
    Timestamp timestamp;

    public Message() {
    }

    public Message(String userName, String message, String sharedImgUrl, Timestamp timestamp) {
        this.userName = userName;
        this.message = message;
        this.sharedImgUrl = sharedImgUrl;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getSharedImgUrl() {
        return sharedImgUrl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
