package com.example.android_exam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.User;

public class SignUpActivity extends AppCompatActivity implements ClientEventHandler {
    private ChatClient _client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        StorageManager storageManager = new StorageManager();

        _client = new ChatClient("127.0.0.1", 4444, this);

        if (storageManager.loadAuthData() != null) {
            Intent intent = new Intent(this, ChatListActivity.class);
            startActivity(intent);
            return;
        }

        findViewById(R.id.button_sign_up).setOnClickListener(this::onButtonSignUpClick);
        findViewById(R.id.text_view_has_account).setOnClickListener(this::onTextViewHasAccountClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
    }

    private void onButtonSignUpClick(View v) {
        String username = ((EditText)findViewById(R.id.edit_text_signup_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.edit_text_signup_password)).getText().toString();
        String confirm = ((EditText)findViewById(R.id.edit_text_signup_password_confirm)).getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_username, Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show();
            return;
        }

        if (password.compareTo(confirm) != 0) {
            Toast.makeText(this, R.string.error_unmatched_password, Toast.LENGTH_LONG).show();
            return;
        }

        ChatClient.getInstance().signUp(new User(username, password));
    }

    private void onTextViewHasAccountClick(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignUp(boolean ok) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_signup, Toast.LENGTH_LONG).show();
            return;
        }

        onTextViewHasAccountClick(null);
    }
}