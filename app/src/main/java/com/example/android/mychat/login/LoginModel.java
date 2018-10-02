package com.example.android.mychat.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginModel {

    interface onAuthenticationFinishedListener{
        void onAuthenticationSuccess(FirebaseUser firebaseUser);
        void onAuthenticationFailed(String error);
        void onCanceled();
    }

    void login(String email,String password,onAuthenticationFinishedListener listener);
    FirebaseUser checkCurrentUser();
}
