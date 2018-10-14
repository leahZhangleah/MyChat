package com.example.android.mychat.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mychat.R;
import com.example.android.mychat.contacts.ContactsActivity;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginView{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mSignInBtn;
    private LoginPresenterImpl loginPresenter;
    private View loginFormView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenterImpl(this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.sign_in_email);
        mPasswordView = (EditText) findViewById(R.id.sign_in_password);
        loginFormView = findViewById(R.id.login_form);
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_NEXT){
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    mPasswordView.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0,0);
                    if(isNetworkAvailable()){
                        loginPresenter.validateAuthentication(mEmailView.getText().toString(),mPasswordView.getText().toString());
                    }else{
                        Snackbar.make(loginFormView, R.string.no_internet_connection,Snackbar.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
        mSignInBtn = (Button) findViewById(R.id.sign_in_or_register_btn);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    loginPresenter.validateAuthentication(mEmailView.getText().toString(),mPasswordView.getText().toString());
                }else{
                    Snackbar.make(loginFormView, R.string.no_internet_connection,Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkAvailable()){
            FirebaseUser firebaseUser = loginPresenter.checkCurrentUser();
            updateUI(firebaseUser);
        }
    }

    private void updateUI(FirebaseUser firebaseUser){
        showProgress(false);
        if(firebaseUser!=null){
            Intent intent = new Intent(this, ContactsActivity.class);
            intent.putExtra("currentUser",firebaseUser);
            startActivity(intent);
        }
    }

    @Override
    public void onEmailInvalid() {
        showProgress(false);
        mEmailView.setError(getString(R.string.invalid_email));
    }

    @Override
    public void onPasswordInvalid() {
        showProgress(false);
        mPasswordView.setError(getString(R.string.invalid_password));
    }

    @Override
    public void onLoginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mPasswordView.setText("");
        if(firebaseUser!=null){
            Intent intent = new Intent(this, ContactsActivity.class);
            intent.putExtra("currentUser",firebaseUser);
            startActivity(intent);
        }
        Toast.makeText(this, getString(R.string.success_login)+firebaseUser.getEmail(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showProgress(boolean show) {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(show? View.VISIBLE:View.INVISIBLE);
        loginFormView.setVisibility(show?View.INVISIBLE:View.VISIBLE);
    }
}
