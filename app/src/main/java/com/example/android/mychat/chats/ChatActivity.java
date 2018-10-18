package com.example.android.mychat.chats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mychat.R;

public class ChatActivity extends AppCompatActivity {
    public static final String COUNTER_CONTACT_EMAIL = "counter_contact_email";
    public static final String COUNTER_CONTACT_UID = "counter_contact_uid";
    public static final String USER_EMAIL = "current_user_email";
    public static final String USER_UID = "current_user_uid";
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    EditText chatEditText;
    ChatViewModel chatViewModel;
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
        fetchMessages(userUid,contactUid,0);

        chatEditText = findViewById(R.id.chat_edit_text_view);
        chatEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String text = chatEditText.getText().toString();
                    if(text!=null){
                        sendMessage(text,userUid,contactUid);
                        chatEditText.setText("");
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0,0);
                    }else{
                        Toast.makeText(ChatActivity.this,"message can't be null",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchMessages(String userUid, String contactUid, int currentPage){
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatViewModel.fetchMessages(userUid, contactUid, currentPage)
                .observe(this, new Observer<Message>() {
                    @Override
                    public void onChanged(@Nullable Message message) {
                        if(message!=null){
                            chatAdapter.fetchMoreMessages(message);
                        }
                    }
                });
    }

    private void sendMessage(String text,String sendUid,String contactUid){
        Message message = new Message(sendUid,text, System.currentTimeMillis());
        chatViewModel.sendMessage(sendUid,contactUid,message);
    }



}
