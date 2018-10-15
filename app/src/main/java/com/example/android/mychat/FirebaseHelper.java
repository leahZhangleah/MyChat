package com.example.android.mychat;

import android.util.Log;

import com.example.android.mychat.contacts.Contact;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private FirebaseDatabase firebasedatabse;
    private FirebaseAuth firebaseAuth;
    public static final String USER_PATH = "users";
    public static final String CONTACTS_PATH = "contacts";
    public static final String CHATS_PATH = "chats";

    public FirebaseHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebasedatabse = FirebaseDatabase.getInstance();
    }

    public DatabaseReference getUserDbReference(){
        return firebasedatabse.getReference(USER_PATH);
    }

    public DatabaseReference getOneUserContactsDbReference(String uid){
        return firebasedatabse.getReference(CONTACTS_PATH).child(uid);
    }

    public DatabaseReference getChatsDbReference(){
        return firebasedatabse.getReference().child(getCurrentUserUid()).child(CHATS_PATH);
    }

    public String getCurrentUserUid(){
        return firebaseAuth.getCurrentUser().getUid();
    }

    public String getCurrentUserEmail(){
        return firebaseAuth.getCurrentUser().getEmail();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void addContactForOneUser(String userUid, String contactUid, Contact contact){
        getOneUserContactsDbReference(userUid).child(contactUid).setValue(contact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"the new contact has been successfully added");
                    }
                });
    }
}
