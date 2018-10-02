package com.example.android.mychat.login;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

public class LoginPresenterImpl implements LoginPresenter,LoginModel.onAuthenticationFinishedListener {
    private LoginView loginView;
    private LoginModelImpl loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
    }


    @Override
    public void validateAuthentication(String email, String password) {
        if(TextUtils.isEmpty(email) || !isEmailValid(email)){
            loginView.onEmailInvalid();
            return;
        }
        if(password.length() < 8){
            loginView.onPasswordInvalid();
            return;
        }
        loginView.showProgress(true);
        loginModel.login(email,password,this);
    }

    @Override
    public FirebaseUser checkCurrentUser() {
        return loginModel.checkCurrentUser();
    }


    private boolean isEmailValid(String email){
        if(email.contains("@") && email.contains(".c")){
            return true;
        }
        return false;
    }


    @Override
    public void onAuthenticationSuccess(FirebaseUser firebaseUser) {
        loginView.showProgress(false);
        loginView.onSuccess(firebaseUser);
    }

    @Override
    public void onAuthenticationFailed(String error) {
        loginView.showProgress(false);
        loginView.onLoginFailed(error);
    }

    @Override
    public void onCanceled() {
        loginView.showProgress(false);
    }
}
