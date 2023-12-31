package com.example.android_exam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.adapters.MessageAdapter;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements ClientEventHandler {
    private MessageAdapter _messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getMessageList();

        _messageAdapter = new MessageAdapter(this);
        ListView listView = findViewById(R.id.list_view_messages);
        listView.setAdapter(_messageAdapter);

        findViewById(R.id.button_send_message).setOnClickListener(this::onButtonSendMessageClick);
        ((TextView)findViewById(R.id.text_view_chat_username)).setText(getIntent().getStringExtra("chatName"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getMessageList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().disconnect();
        ChatClient.getInstance().resetHandler(this);
    }

    @Override
    public void onGetMessageList(boolean ok, List<Message> messages) {
        if (!ok)
            return;

        _messageAdapter.setMessages(messages);
    }

    @Override
    public void onGetMessage(boolean ok, Message message) {
        if (!ok)
            return;

        if (_messageAdapter != null)
            _messageAdapter.addMessage(message);
    }

    private void onButtonSendMessageClick(View v) {
        EditText editTextMessageBody = (EditText)findViewById(R.id.edit_text_message);
        String messageBody = editTextMessageBody.getText().toString();
        User currentUser = ChatClient.getInstance().getCurrentUser();

        ChatClient.getInstance().sendMessage(messageBody);
        editTextMessageBody.setText("");

        _messageAdapter.addMessage(new Message(currentUser.getUsername(), messageBody, new Date()));
    }
}