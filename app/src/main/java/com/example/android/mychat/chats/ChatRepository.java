package com.example.android.mychat.chats;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mychat.FirebaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatRepository {
    private static final String TAG = "ChatRepository";
    private FirebaseHelper firebaseHelper;
    private MutableLiveData<DataSnapshot> snapshotMutableLiveData;
    private static final int MESSAGE_COUNT = 11;

    public ChatRepository() {
        firebaseHelper = new FirebaseHelper();
        snapshotMutableLiveData = new MutableLiveData<>();
    }
    //.limitToFirst(MESSAGE_COUNT)
    //                .startAt(currentPage * MESSAGE_COUNT + 1);

    public MutableLiveData<DataSnapshot> fetchMoreMessages(String uniqueKey,String lastVisibleKey){
        Query query = firebaseHelper.getChatsDbReference(uniqueKey)
                .limitToLast(MESSAGE_COUNT)
                .orderByKey()
                .endAt(lastVisibleKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               snapshotMutableLiveData.postValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return snapshotMutableLiveData;
    }
    public FirebaseQueryLiveData fetchMessages(String uniqueKey,int currentPage){
        int page = currentPage * MESSAGE_COUNT + 1;
        Query query = firebaseHelper.getChatsDbReference(uniqueKey)
                .limitToLast(MESSAGE_COUNT)
                .orderByKey();
        Log.d(TAG,query.toString());
        Log.d(TAG,query.getRef().toString());
        return new FirebaseQueryLiveData(query);
        /*
        firebaseHelper.getChatsDbReference(uniqueKey).limitToFirst(MESSAGE_COUNT)
                .startAt(currentPage * MESSAGE_COUNT + 1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        newMsg = dataSnapshot.getValue(Message.class);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

        //return newMsg;
    }

    public void sendMessage(String uniqueKey, Message msg){
        firebaseHelper.getChatsDbReference(uniqueKey).push().setValue(msg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"message has been added successfully");
                    }
                });
    }

    /*
    .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messages.clear();
                        for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                            Message message = messageSnapshot.getValue(Message.class);
                            messages.add(message);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
     */
}
