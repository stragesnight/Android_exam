package com.example.android_exam.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import com.example.android_exam.R;
import com.example.android_exam.activities.ChatListActivity;
import com.example.android_exam.models.Chat;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private List<Chat> _chats;


    public ChatAdapter(@NonNull Context context, List<Chat> chats) {
        super(context, R.layout.layout_chat, R.id.chat_name);
        _chats = chats;
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
