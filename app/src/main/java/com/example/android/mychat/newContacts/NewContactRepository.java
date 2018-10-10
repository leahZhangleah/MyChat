package com.example.android.mychat.newContacts;

import android.support.annotation.NonNull;

import com.example.android.mychat.FirebaseHelper;
import com.example.android.mychat.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NewContactRepository {

    FirebaseHelper firebaseHelper;
    Observable<List<User>> usersObservable;
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
    public Observable<List<User>> searchNewContact(String searchTerm){
        firebaseHelper.getUserDbReference().orderByChild("email")
                .startAt(searchTerm).endAt(searchTerm+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            users.add(user);
                        }
                        usersObservable = Observable.create(new ObservableOnSubscribe<List<User>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                                if(!emitter.isDisposed()){
                                    emitter.onNext(users);
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return usersObservable;
    }
}
