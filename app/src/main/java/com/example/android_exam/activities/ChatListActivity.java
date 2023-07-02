package com.example.android_exam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.adapters.ChatAdapter;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.Chat;

import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ClientEventHandler {
    private ChatAdapter _adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getChatList();

        ListView listViewChats = findViewById(R.id.list_view_chats);
        _adapter = new ChatAdapter(this);
        listViewChats.setAdapter(_adapter);

        findViewById(R.id.button_add_chat).setOnClickListener(this::onButtonAddChatClick);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getChatList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
    }

    @Override
    public void onGetChatList(boolean ok, List<Chat> chats) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_get_chat_list, Toast.LENGTH_LONG).show();
            return;
        }

        _adapter.setChats(chats);
    }

    public void onChatClick(Chat chat) {
        ChatClient.getInstance().connect(chat.getName());
    }

    @Override
    public void onConnect(boolean ok, Chat chat) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_connect, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatName", chat.getName());
        startActivity(intent);
    }

    private void onButtonAddChatClick(View v) {
        Intent intent = new Intent(this, CreateChatActivity.class);
        startActivity(intent);
    }
}