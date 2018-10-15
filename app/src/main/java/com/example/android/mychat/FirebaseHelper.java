package com.example.android.mychat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

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
}
