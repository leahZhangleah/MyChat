package com.example.android.mychat.newContacts;

import android.view.View;

import com.example.android.mychat.User;

public class SearchItemViewModel {
    User user;
    NewContactRepository newContactRepository;

    public SearchItemViewModel(User user) {
        this.user = user;
        newContactRepository = new NewContactRepository();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getEmailBrev(){
        return user.getEmail().substring(0,1);
    }

    public void onClick(View view){
        newContactRepository.addNewContact(user);
    }


}
