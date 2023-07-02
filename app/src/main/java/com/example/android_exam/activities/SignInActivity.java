package com.example.android_exam.activities;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.User;

public class SignInActivity extends AppCompatActivity implements ClientEventHandler {
    private StorageManager _storageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ChatClient.getInstance().setHandler(this);
        _storageManager = new StorageManager(this);

        findViewById(R.id.button_sign_in).setOnClickListener(this::onButtonSignInClick);
        findViewById(R.id.text_view_has_no_account).setOnClickListener(this::onTextViewHasNoAccountClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
    }

    private void onButtonSignInClick(View v) {
        String username = ((EditText)findViewById(R.id.edit_text_signin_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.edit_text_signin_password)).getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_username, Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show();
            return;
        }

        ChatClient.getInstance().signIn(new User(username, password));
    }

    private void onTextViewHasNoAccountClick(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignIn(boolean ok) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_signin, LENGTH_LONG).show();
            return;
        }

        _storageManager.saveAuthData(ChatClient.getInstance().getCurrentUser());

        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
    }
}