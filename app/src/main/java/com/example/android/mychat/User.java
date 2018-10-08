package com.example.android.mychat;

public class User {
    String email,username,uid;

    public User() {
    }


    public User(String email, String username, String uid) {
        this.email = email;
        this.username = username;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
