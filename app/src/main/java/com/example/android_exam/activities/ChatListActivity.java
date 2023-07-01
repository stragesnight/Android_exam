package com.example.android_exam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

        findViewById(R.id.button_add_chat).setOnClickListener(this::onButtonAddChatClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
    }

    @Override
    public void onGetChatList(boolean ok, List<Chat> chats) {
        ListView listViewChats = findViewById(R.id.list_view_chats);
        _adapter = new ChatAdapter(this, chats);
        listViewChats.setAdapter(_adapter);
    }

    public void onChatClick(Chat chat) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatName", chat.getName());
        startActivity(intent);
    }

    private void onButtonAddChatClick(View v) {
        // TODO
    }
}