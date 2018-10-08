package com.example.android.mychat.contacts;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mychat.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final String TAG = "Repository";
    private DatabaseReference mDatabase;
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<List<Contact>> contactListsLiveData;
    List<Contact> contacts;
    ValueEventListener contactListener;

    public Repository() {
        firebaseHelper = new FirebaseHelper();
        mDatabase = firebaseHelper.getContactsDbReference();
    }

    public void addNewContact(String email, String uid){
        Contact contact = new Contact(email,uid);
        mDatabase.child(uid).setValue(contact);

    }

    public MutableLiveData<List<Contact>> getContactListsLiveData() {
        contacts = new ArrayList<>();
        contactListsLiveData = new MutableLiveData<>();
        contactListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,dataSnapshot.getKey());
                Log.d(TAG,dataSnapshot.getValue().toString());
                Log.d(TAG,dataSnapshot.getRef().toString());
                Log.d(TAG,dataSnapshot.getRef().getRoot().toString());
                Log.d(TAG,dataSnapshot.getChildren().toString());
                Log.d(TAG,dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot contactSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG,contactSnapshot.getKey());
                    Log.d(TAG,contactSnapshot.getValue(Contact.class).email);
                    Log.d(TAG,contactSnapshot.getValue(Contact.class).uid);
                    Log.d(TAG,contactSnapshot.getRef().toString());
                    Log.d(TAG,contactSnapshot.getRef().getRoot().toString());
                    //String uid = contactSnapshot.getKey();
                    Contact contact = contactSnapshot.getValue(Contact.class);
                    contacts.add(contact);
                }
                contactListsLiveData.postValue(contacts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // todo: Getting contact failed, log a message
                Log.d(TAG,"load contacts: cancelled",databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(contactListener);
        return contactListsLiveData;
    }

    public void removeEventListener(){
        mDatabase.removeEventListener(contactListener);
    }
}
