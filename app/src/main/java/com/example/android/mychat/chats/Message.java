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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return timestamp == message.timestamp && senderUid == message.senderUid && msg == message.msg;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + senderUid.hashCode();
        hash = 31 * hash + msg.hashCode();
        hash = 31 * hash + (int)timestamp;
        return hash;
    }
}
