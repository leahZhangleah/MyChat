package com.example.android.mychat.newContacts;

import com.example.android.mychat.User;

public class SearchItemViewModel {
    User user;

    public SearchItemViewModel(User user) {
        this.user = user;
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getEmailBrev(){
        return user.getEmail().substring(0,1);
    }


}
