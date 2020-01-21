package com.example.android.mychat.newContacts;

import android.support.annotation.NonNull;

import com.example.android.mychat.FirebaseHelper;
import com.example.android.mychat.User;
import com.example.android.mychat.contacts.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class NewContactRepository {
    private static final String TAG = "NewContactRepository";
    FirebaseHelper firebaseHelper;
    List<User> users;


    public NewContactRepository() {
        this.firebaseHelper = new FirebaseHelper();
        users = new ArrayList<>();
    }

    /*
    firebase doesn't support blur search, which needs the help of Agolia
    https://github.com/firebase/functions-samples/tree/master/fulltext-search
    exact search: orderByChild("email").equalTo(searchTerm)
    Here is exact search: start with searchTerm
     */
    public List<User> searchNewContact(String searchTerm){
        firebaseHelper.getUserDbReference().orderByChild("email")
                .startAt(searchTerm).endAt(searchTerm+"\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            users.add(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return users;
    }


    public void addNewContact(User user){
        //add new contact for current user
        String email = user.getEmail();
        String uid = user.getUid();
        boolean isOnline = user.isOnline();
        Contact contact = new Contact(email,isOnline,uid);
        String currentUid = firebaseHelper.getCurrentUserUid();
        String currentEmail = firebaseHelper.getCurrentUserEmail();
        boolean currentUserOnlineStatus = true;
        Contact currentContact = new Contact(currentEmail,currentUserOnlineStatus,currentUid);
        firebaseHelper.addContactForOneUser(currentUid,uid,contact);
        firebaseHelper.addContactForOneUser(uid,currentUid,currentContact);

        //add chat database for current user and contact
       saveUsersInChatroomDb(currentUid,uid);

        EventBus.getDefault().post(new NewContactEvent());
    }

    private void saveUsersInChatroomDb(String userUid, String contactUid){
        String uniqueKey;
        if(userUid.compareTo(contactUid) < 0){
            uniqueKey =  userUid + contactUid;
        }else{
            uniqueKey = contactUid + userUid;
        }
        firebaseHelper.getChatroomDbReference(uniqueKey).child(userUid).setValue(true);
        firebaseHelper.getChatroomDbReference(uniqueKey).child(contactUid).setValue(true);
    }

}