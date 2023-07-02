package com.example.android_exam.activities;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;

import com.example.android_exam.R;
import com.example.android_exam.adapters.UserAdapter;
import com.example.android_exam.domain.*;
import com.example.android_exam.models.Chat;
import com.example.android_exam.models.User;

import java.util.List;

public class CreateChatActivity extends AppCompatActivity implements ClientEventHandler {
    private UserAdapter _userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        ChatClient.getInstance().setHandler(this);
        ChatClient.getInstance().getUserList();

        ((EditText)findViewById(R.id.edit_text_search_username)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                _userAdapter.applyFilter(s.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClient.getInstance().resetHandler(this);
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

    @Override
    public void onGetUserList(boolean ok, List<User> users) {
        if (!ok) {
            Toast.makeText(this, R.string.error_unable_to_get_user_list, LENGTH_LONG).show();
            return;
        }

        _userAdapter = new UserAdapter(this, users);
        ListView listView = findViewById(R.id.list_view_users);
        listView.setAdapter(_userAdapter);
    }

    public void onUserClick(User user) {
        ChatClient.getInstance().connect(user.getUsername());
    }
}
