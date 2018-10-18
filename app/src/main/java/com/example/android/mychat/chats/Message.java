package com.example.android.mychat.chats;

public class Message {
    String senderUid,msg;
    long timestamp;

    public Message() {
    }

    public Message(String senderUid, String msg, long timestamp) {
        this.senderUid = senderUid;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getMsg() {
        return msg;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
