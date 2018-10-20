package com.example.android.mychat.chats;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private static final String TAG = "ChatViewModel";
    private ChatRepository chatRepository;
    //private MutableLiveData<Message> messageLiveData;
    private List<String> keyList;
    private List<Message> newMsgsList;

    public ChatViewModel() {
        chatRepository = new ChatRepository();
        //messageLiveData = new MutableLiveData<>();
        keyList = new ArrayList<>();
        newMsgsList = new ArrayList<>();
    }

    public LiveData<Message> fetchMessages(String userUid, String contactUid, int currentPage){
        String uniqueKey = createUniqueKey(userUid, contactUid);
        FirebaseQueryLiveData liveData = chatRepository.fetchMessages(uniqueKey,currentPage);
       return Transformations.map(liveData, new Function<DataSnapshot, Message>() {
            @Override
            public Message apply(DataSnapshot input) {
                keyList.add(input.getKey());
                Log.d(TAG,"the received msg is: "+input.getValue(Message.class));
                return input.getValue(Message.class);
            }
        });
    }

    public LiveData<List<Message>> fetchMoreMessages(String userUid, String contactUid){
        String uniqueKey = createUniqueKey(userUid, contactUid);
        //when there's no msg between these two users
        if(keyList.isEmpty()){
            return null;
        }
        String lastVisibleKey = keyList.get(0);
        MutableLiveData<DataSnapshot> dataSnapshotMutableLiveData = chatRepository.fetchMoreMessages(uniqueKey,lastVisibleKey);
        return Transformations.map(dataSnapshotMutableLiveData,
                new Function<DataSnapshot, List<Message>>() {
            @Override
            public List<Message> apply(DataSnapshot input) {
                newMsgsList.clear();
                keyList.clear();
                for(DataSnapshot messageSnapshot: input.getChildren()){
                    Message message = messageSnapshot.getValue(Message.class);
                    newMsgsList.add(message);
                    keyList.add(messageSnapshot.getKey());
                }
                return newMsgsList;
            }
        });
    }

    private String createUniqueKey(String userUid, String contactUid){
        if(userUid.compareTo(contactUid) < 0){
            return userUid + contactUid;
        }else{
            return contactUid + userUid;
        }
    }

    public void sendMessage(String userUid, String contactUid,Message msg){
        String uniqueKey = createUniqueKey(userUid, contactUid);
        chatRepository.sendMessage(uniqueKey,msg);
    }


}
