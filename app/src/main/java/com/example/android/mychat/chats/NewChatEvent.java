package com.example.android.mychat.chats;

import com.example.android.mychat.contacts.Contact;

public class NewChatEvent {
    Contact contact;

    public NewChatEvent(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
