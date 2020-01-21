package com.example.android.mychat.chats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mychat.R;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public static final String COUNTER_CONTACT_EMAIL = "counter_contact_email";
    public static final String COUNTER_CONTACT_UID = "counter_contact_uid";
    public static final String USER_EMAIL = "current_user_email";
    public static final String USER_UID = "current_user_uid";
    private static final int MESSAGE_COUNT = 11;
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    EditText chatEditText;
    ChatViewModel chatViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    //List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String contactEmail = intent.getStringExtra(COUNTER_CONTACT_EMAIL);
        final String contactUid = intent.getStringExtra(COUNTER_CONTACT_UID);
        String userEmail = intent.getStringExtra(USER_EMAIL);
        final String userUid = intent.getStringExtra(USER_UID);
        //messageList = new ArrayList<>();

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        chatAdapter = new ChatAdapter(userUid,contactUid);
        chatRecyclerView.setAdapter(chatAdapter);
        swipeRefreshLayout = findViewById(R.id.chat_refresh_layout);
        fetchMessages(userUid,contactUid,0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoreMessages(userUid,contactUid);
            }
        });

        chatEditText = findViewById(R.id.chat_edit_text_view);
        chatEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String text = chatEditText.getText().toString();
                    if(text==null || TextUtils.isEmpty(text) || text.equals("")){
                        Toast.makeText(ChatActivity.this,"message can't be null",Toast.LENGTH_SHORT).show();
                    }else{
                        sendMessage(text,userUid,contactUid);
                        chatEditText.setText("");
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0,0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //todo: 1.scroll recyclerview to the right position when first load and fetch more msgs
    //todo: 2.adjust layout when soft keyboard appears

    private void fetchMessages(String userUid, String contactUid, int currentPage){
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatViewModel.fetchMessages(userUid, contactUid, currentPage)
                .observe(this, new Observer<Message>() {
                    @Override
                    public void onChanged(@Nullable Message message) {
                        if(message!=null){
                            chatAdapter.fetchMessages(message);
                        }
                    }
                });
        swipeRefreshLayout.setRefreshing(false);
    }

    private void fetchMoreMessages(String userUid, String contactUid){
        if(chatViewModel.fetchMoreMessages(userUid, contactUid)==null){
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
            Toast.makeText(this,"No msg available",Toast.LENGTH_SHORT).show();
            return;
        }
        chatViewModel.fetchMoreMessages(userUid, contactUid)
                .observe(this, new Observer<List<Message>>() {
                    @Override
                    public void onChanged(@Nullable List<Message> messages) {
                        if(messages!=null){
                            if(messages.size()<MESSAGE_COUNT){
                                swipeRefreshLayout.setEnabled(false);
                            }
                            //to deal with the duplication of last visible item
                            for(int pos = 0; pos < messages.size()-1; pos++){
                                chatAdapter.fetchMoreMessages(pos,messages.get(pos));
                            }
                        }
                    }
                });
        swipeRefreshLayout.setRefreshing(false);
    }

    private void sendMessage(String text,String sendUid,String contactUid){
        Message message = new Message(sendUid,text, System.currentTimeMillis());
        chatViewModel.sendMessage(sendUid,contactUid,message);
    }



}
