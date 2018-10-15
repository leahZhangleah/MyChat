package com.example.android.mychat.newContacts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.mychat.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NewContactActivity extends AppCompatActivity {
    EditText searchTextView;
    NewContactViewModel newContactViewModel;
    RecyclerView searchRv;
    TextView searchMsgV;
    ProgressBar searchProgress;
    SearchAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        searchTextView = (EditText) findViewById(R.id.searchTextView);
        searchMsgV = findViewById(R.id.search_message_view);
        searchProgress = findViewById(R.id.search_progress);
        newContactViewModel = ViewModelProviders.of(this).get(NewContactViewModel.class);
        searchRv = findViewById(R.id.search_recycler_view);
        searchRv.setLayoutManager(
                new LinearLayoutManager(NewContactActivity.this,
                        LinearLayoutManager.VERTICAL,false));
        searchAdapter = new SearchAdapter();
        searchRv.setAdapter(searchAdapter);

        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    searchTextView.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0,0);
                    search(searchTextView.getText().toString());
                    return true;
                }
                return false;
            }
        });
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void search(String searchTerm){
        SearchResponse searchResponse = newContactViewModel.createPublishObservable(searchTerm);
        switch (searchResponse.status){
            case LOADING:
                return;
            case SUCCESS:
                searchProgress.setVisibility(View.INVISIBLE);
                if(searchResponse.data.isEmpty()){
                    searchMsgV.setVisibility(View.VISIBLE);
                }else{
                    searchMsgV.setVisibility(View.INVISIBLE);
                }
                searchAdapter.switchUsers(searchResponse.data);
                return;
            case ERROR:
                searchProgress.setVisibility(View.INVISIBLE);
                searchMsgV.setVisibility(View.VISIBLE);
                return;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewContactEvent(NewContactEvent event){
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
