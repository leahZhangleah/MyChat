package com.example.android.mychat.chats;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String TAG = "FirebaseQueryLiveData";
    private Query query;
    private boolean removeListenerPending = false;
    private Handler handler = new Handler();
    private MyChildEventListener myValueEventListener = new MyChildEventListener();
    private Runnable removeEventListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(myValueEventListener);
            removeListenerPending = false;
        }
    };

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference databaseReference) {
        this.query = databaseReference;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(removeListenerPending){
            handler.removeCallbacks(removeEventListener);
        }else{
            query.addChildEventListener(myValueEventListener);
        }
        removeListenerPending = false;
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        //wait 2000 milliseconds to see if the oninactive is caused by configuration change
        handler.postDelayed(removeEventListener,2000);
        removeListenerPending = true;
    }

    private class MyChildEventListener implements ChildEventListener{

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            setValue(dataSnapshot);
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
            Log.d(TAG,"Can't listen to query. "+query + databaseError.toException());
        }
    }

}
