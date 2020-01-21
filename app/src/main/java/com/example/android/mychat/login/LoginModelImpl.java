package com.example.android.mychat.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mychat.FirebaseHelper;
import com.example.android.mychat.User;
import com.example.android.mychat.contacts.Contact;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginModelImpl implements LoginModel {
    FirebaseAuth firebaseAuth;
    FirebaseHelper firebaseHelper;
    private static final String TAG = "LoginModelImpl";
    private String currentTokenId;

    public LoginModelImpl() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    public FirebaseUser checkCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    //firebase deal with the background setting thing
    @Override
    public void login(final String email, final String password, final onAuthenticationFinishedListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("LoginModelImpl","sign in user with email:success");
                        String currentUserUid = authResult.getUser().getUid();
                        updateTokenId(currentUserUid);
                        listener.onAuthenticationSuccess(authResult.getUser());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LoginModelImpl","sign in user with email:failure", e.getCause());
                        createNewUser(email, password, listener);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("LoginModelImpl","sign in user with email:cancelled");
                        listener.onCanceled();
                    }
                });
    }

    private void createNewUser(final String email, String password,final onAuthenticationFinishedListener listener){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("LoginModelImpl","sign up user with email:success");
                        String currentUserUid = authResult.getUser().getUid();
                        User user = new User(email,true,currentUserUid);
                        firebaseHelper.getUserDbReference().child(currentUserUid).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"The new user has been added into the database");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"adding user into database failed with error: "+e.getMessage());
                                    }
                                })
                                .addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Log.d(TAG,"adding user into database is cancelled");
                                    }
                                });
                        Contact contact = new Contact(email,true,currentUserUid);
                        firebaseHelper.getOneUserContactsDbReference(firebaseHelper.getCurrentUserUid()).push().setValue(contact);
                        updateTokenId(currentUserUid);
                        listener.onAuthenticationSuccess(authResult.getUser());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LoginModelImpl","sign up user with email:failure",e.getCause());
                        listener.onAuthenticationFailed("Can't create a new account");
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("LoginModelImpl","sign up user with email:cancelled");
                        listener.onCanceled();
                    }
                });
    }

    private void updateTokenId(final String currentUserUid){
        FirebaseUser currentUser = checkCurrentUser();
        currentUser.getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        currentTokenId = getTokenResult.getToken();
                        firebaseHelper.getUserDbReference().child(currentUserUid)
                                .child("device_token").setValue(currentTokenId);
                    }
                });
    }

}



























