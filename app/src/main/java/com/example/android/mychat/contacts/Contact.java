package com.example.android.mychat.contacts;

public class Contact {
    public String email,uid;

    public Contact() {
    }

    public Contact(String email,String uid) {
        this.email = email;
        this.uid = uid;
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
