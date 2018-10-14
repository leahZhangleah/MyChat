package com.example.android.mychat;

public class User {
    String email,uid;
    boolean isOnline;

    public User() {
    }


    public User(String email, boolean isOnline, String uid) {
        this.email = email;
        this.isOnline = isOnline;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }


    public boolean isOnline() {
        return isOnline;
    }
}
