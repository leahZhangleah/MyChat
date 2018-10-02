package com.example.android.mychat.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginPresenter {
    void validateAuthentication(String email,String password);
    FirebaseUser checkCurrentUser();
}
