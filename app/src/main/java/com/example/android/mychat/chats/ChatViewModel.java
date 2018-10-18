package com.example.android.mychat.chats;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.android.mychat.newContacts.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class ChatViewModel extends ViewModel {
    ChatRepository chatRepository;
    MutableLiveData<Message> messageLiveData;
    List<Message> messageList;
    Message newMsg;
    Observable<Message> messageObservable;


    public ChatViewModel() {
        chatRepository = new ChatRepository();
        messageLiveData = new MutableLiveData<>();
        messageList = new ArrayList<>();

    }

    public LiveData<Message> fetchMessages(String userUid, String contactUid, int currentPage){
        String uniqueKey = createUniqueKey(userUid, contactUid);
        FirebaseQueryLiveData liveData = chatRepository.fetchMessages(uniqueKey,currentPage);
        LiveData<Message> messageLiveData = Transformations.map(liveData, new Function<DataSnapshot, Message>() {
            @Override
            public Message apply(DataSnapshot input) {
                return input.getValue(Message.class);
            }
        });
        return messageLiveData;
            //messageList.clear();
            //messageList.add(newMsg);
        //messageLiveData.postValue(newMsg);
        //return messageLiveData;
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
