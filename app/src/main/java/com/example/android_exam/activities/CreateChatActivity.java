package com.example.android_exam.activities;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.Chat;

public class CreateChatActivity extends AppCompatActivity implements ClientEventHandler {
    private String _chatName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        ChatClient.getInstance().setHandler(this);

        findViewById(R.id.button_go_add_chat).setOnClickListener(this::onButtonGoAddChatClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
    }

    @Override
    public void onCreateChat(boolean ok) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_add_chat, LENGTH_LONG).show();
            return;
        }

        ChatClient.getInstance().connect(_chatName);
    }

    @Override
    public void onConnect(boolean ok, Chat chat) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_connect, LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatName", chat.getName());
        startActivity(intent);
    }

    private void onButtonGoAddChatClick(View v) {
        _chatName = ((EditText)findViewById(R.id.edit_text_chat_name)).getText().toString();
        if (_chatName.trim().isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_chat_name, LENGTH_LONG).show();
            return;
        }

        ChatClient.getInstance().addChat(_chatName);
    }
}
