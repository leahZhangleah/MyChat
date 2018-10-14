package com.example.android.mychat.newContacts;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String TAG = "FirebaseQueryLiveData";
    private Query query;
    private boolean removeListenerPending = false;
    private Handler handler = new Handler();
    private MyValueEventListener myValueEventListener = new MyValueEventListener();
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
            query.addValueEventListener(myValueEventListener);
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

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG,"Can't listen to query. "+query + databaseError.toException());
        }
    }
}
