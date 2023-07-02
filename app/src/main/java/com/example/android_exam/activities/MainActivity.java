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

        findViewById(R.id.button_send_message).setOnClickListener(this::onButtonSendMessageClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().disconnect();
        ChatClient.getInstance().resetHandler(this);
    }

    @Override
    public void onGetMessageList(boolean ok, List<Message> messages) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_get_message_list, Toast.LENGTH_LONG).show();
            return;
        }

        _messageAdapter = new MessageAdapter(this, messages);
        ListView listView = findViewById(R.id.list_view_messages);
        listView.setAdapter(_messageAdapter);
    }

    @Override
    public void onGetMessage(boolean ok, Message message) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_get_message_list, Toast.LENGTH_LONG).show();
            return;
        }

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