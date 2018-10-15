package com.example.android.mychat.contacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mychat.R;
import com.example.android.mychat.chats.ChatActivity;
import com.example.android.mychat.chats.NewChatEvent;
import com.example.android.mychat.login.LoginActivity;
import com.example.android.mychat.newContacts.NewContactActivity;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class ContactsActivity extends AppCompatActivity {

    private RecyclerView contactRv;
    private ContactsAdapter contactsAdapter;
    private ContactViewModel contactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        FirebaseUser firebaseUser = intent.getParcelableExtra("currentUser");
        ImageView titleImage = (ImageView) toolbar.findViewById(R.id.toolbar_image);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(firebaseUser.getEmail().split("@")[0]);
        contactRv = (RecyclerView) findViewById(R.id.contacts_rv);
        contactRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        contactsAdapter = new ContactsAdapter();
        contactRv.setAdapter(contactsAdapter);
        readDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contactViewModel.addNewContact("c@gmail.com","asdbdm");
                //contactViewModel.addNewContact("d@gmail.com","weitnd");
                //contactViewModel.writeToDatabase("c@gmail.com","Do you wanna hang ou?","07/28",null);
                //contactViewModel.writeToDatabase("d@gmail.com","Sure,what's your plan?","07/28",null);
                addUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void addUser(){
        Intent intent = new Intent(this,NewContactActivity.class);
        startActivity(intent);
    }

    private void readDatabase(){
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                contactsAdapter.switchContacts(contacts);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                contactViewModel.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewChatEvent(NewChatEvent event){
        Intent intent = new Intent(this, ChatActivity.class);
        Contact contact = event.getContact();
        String email = contact.getEmail();
        String uid = contact.getUid();
        intent.putExtra(ChatActivity.COUNTER_CONTACT_EMAIL,email);
        intent.putExtra(ChatActivity.COUNTER_CONTACT_UID,uid);
        startActivity(intent);
    }


}
