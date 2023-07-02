package com.example.android_exam.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import com.example.android_exam.R;
import com.example.android_exam.activities.ChatListActivity;
import com.example.android_exam.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private List<Chat> _chats = null;


    public ChatAdapter(@NonNull Context context) {
        super(context, R.layout.layout_chat, R.id.chat_name);
    }

    public void setChats(List<Chat> chats) {
        _chats = new ArrayList<>(chats);
        clear();
        addAll(chats);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Chat chat = _chats.get(position);

        ((TextView)view.findViewById(R.id.chat_name)).setText(chat.getName());
        ((TextView)view.findViewById(R.id.chat_last_message)).setText(chat.getLastMessage());
        view.setOnClickListener(v -> ((ChatListActivity)getContext()).onChatClick(chat));

        return view;
    }
}
