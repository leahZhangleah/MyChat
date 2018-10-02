package com.example.android.mychat.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginView {
    void onEmailInvalid();
    void onPasswordInvalid();
    void onLoginFailed(String error);
    void onSuccess(FirebaseUser firebaseUser);
    void showProgress(boolean show);
}
