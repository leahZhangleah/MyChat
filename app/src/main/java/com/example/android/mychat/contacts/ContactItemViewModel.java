package com.example.android.mychat.contacts;

import android.view.View;

import com.example.android.mychat.chats.NewChatEvent;

import org.greenrobot.eventbus.EventBus;

public class ContactItemViewModel {
    private Contact contact;

    public ContactItemViewModel(Contact contact) {
        this.contact = contact;
    }
    public String getEmail(){
        return contact.getEmail();
    }

    public String getEmailBrev(){
        return contact.getEmail().substring(0,1);
    }

    public void onClick(View view){
        EventBus.getDefault().post(new NewChatEvent(contact));
    }
}
