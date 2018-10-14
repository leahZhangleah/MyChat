package com.example.android.mychat.contacts;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private static final String TAG = "ContactViewModel";
    private Repository repository;
    private LiveData<List<Contact>> contactsLiveData;

    public ContactViewModel() {
        repository = new Repository();

    }

    public LiveData<List<Contact>> getContacts(){
        return Transformations.map(repository.getContactListsLiveData(), new Function<List<Contact>, List<Contact>>() {
            @Override
            public List<Contact> apply(List<Contact> input) {
                for(Contact contact:input){
                    Log.d(TAG,contact.email+contact.uid);
                }
                return input;
            }
        });
    }

/*
    public void addNewContact(String email,String uid){
        repository.addNewContact(email,uid);
    }*/

    public void signOut(){
        repository.signOut();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.removeEventListener();
    }
}
