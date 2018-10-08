package com.example.android.mychat.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginModelImpl implements LoginModel {
    FirebaseAuth firebaseAuth;

    public LoginModelImpl() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public FirebaseUser checkCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    //firebase deal with the background setting thing
    @Override
    public void login(final String email, String password, final onAuthenticationFinishedListener listener) {
        if( firebaseAuth.fetchSignInMethodsForEmail(email).isSuccessful()){
            Log.d("LoginModelImpl","the email exists");
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("LoginModelImpl","sign in user with email:success");
                                //can replace this with eventbus, same below
                                listener.onAuthenticationSuccess(firebaseAuth.getCurrentUser());
                            }else{
                                Log.d("LoginModelImpl","sign in user with email:failure",task.getException());
                                listener.onAuthenticationFailed("Wrong password");
                            }
                        }
                    });
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task< AuthResult > task) {
                            if(task.isSuccessful()){
                                Log.d("LoginModelImpl","sign up user with email:success");
                                //todo
                                String currentUserUid = firebaseAuth.getCurrentUser().getUid();
                                FirebaseDatabase.getInstance().getReference("users").child(currentUserUid);
                                listener.onAuthenticationSuccess(firebaseAuth.getCurrentUser());
                            }else{
                                Log.d("LoginModelImpl","sign in user with email:failure",task.getException());
                                listener.onAuthenticationFailed("Can't create a new account");
                            }
                        }
                    });
        }
    }



    /*
    private class SignUpTask extends AsyncTask<Void,Void,Void>{
        String email,password;
        onAuthenticationFinishedListener listener;

        public SignUpTask(String email, String password,onAuthenticationFinishedListener listener) {
            this.email = email;
            this.password = password;
            this.listener = listener;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("LoginModelImpl","sign up user with email:success");
                                listener.onAuthenticationSuccess(firebaseAuth.getCurrentUser());
                            }else{
                                Log.d("LoginModelImpl","sign in user with email:failure",task.getException());
                                listener.onAuthenticationFailed("Can't create a new account");
                            }
                        }
                    });
            return null;
        }
    }

    private class SignInTask extends AsyncTask<Void,Void,Void>{
        String email,password;
        onAuthenticationFinishedListener listener;

        public SignInTask(String email, String password,onAuthenticationFinishedListener listener) {
            this.email = email;
            this.password = password;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if( firebaseAuth.fetchSignInMethodsForEmail(email).isSuccessful()){
                Log.d("LoginModelImpl","the email exists");
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("LoginModelImpl","sign in user with email:success");
                                    listener.onAuthenticationSuccess(firebaseAuth.getCurrentUser());
                                }else{
                                    Log.d("LoginModelImpl","sign in user with email:failure",task.getException());
                                    listener.onAuthenticationFailed("Wrong password");
                                }
                            }
                        });
            }else{
                new SignUpTask(email,password,listener).execute();
            }
            return null;
        }
    }*/
}



























