package com.example.android.mychat.contacts;

public class Contact {
    public String email,uid;
    boolean isOnline;

    public Contact() {
    }

    public Contact(String email,boolean isOnline, String uid) {
        this.email = email;
        this.uid = uid;
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
