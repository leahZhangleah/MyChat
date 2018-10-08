package com.example.android.mychat.contacts;

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
}
