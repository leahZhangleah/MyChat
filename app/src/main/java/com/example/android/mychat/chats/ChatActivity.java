package com.example.android.mychat.chats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.mychat.R;

public class ChatActivity extends AppCompatActivity {
    public static final String COUNTER_CONTACT_EMAIL = "counter_contact_email";
    public static final String COUNTER_CONTACT_UID = "counter_contact_uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
