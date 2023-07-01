package com.example.android_exam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android_exam.R;
import com.example.android_exam.adapters.ChatAdapter;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.Chat;

import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ClientEventHandler {
    private ChatAdapter _adapter;
    private Chat _connectCandidate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getChatList();

        findViewById(R.id.button_add_chat).setOnClickListener(this::onButtonAddChatClick);
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

        ListView listViewChats = findViewById(R.id.list_view_chats);
        _adapter = new ChatAdapter(this, chats);
        listViewChats.setAdapter(_adapter);
    }

    public void onChatClick(Chat chat) {
        ChatClient.getInstance().connect(chat.getName());
        _connectCandidate = chat;
    }

    @Override
    public void onConnect(boolean ok) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_connect, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatName", _connectCandidate.getName());
        startActivity(intent);
    }

    private void onButtonAddChatClick(View v) {
        // TODO
    }
}