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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mychat.R;
import com.example.android.mychat.newContacts.NewContactActivity;
import com.google.firebase.auth.FirebaseUser;

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

    private void addUser(){
        Intent intent = new Intent(this,NewContactActivity.class);
        startActivity(intent);
    }

    private void readDatabase(){
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                contactsAdapter = new ContactsAdapter(contacts);
                contactsAdapter.notifyDataSetChanged();
                contactRv.setAdapter(contactsAdapter);
            }
        });
    }

}
