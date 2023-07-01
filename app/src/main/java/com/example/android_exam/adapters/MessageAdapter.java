package com.example.android_exam.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import com.example.android_exam.models.Message;
import com.example.android_exam.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private List<Message> _messages;


    public MessageAdapter(@NonNull Context context, List<Message> messages) {
        super(context, R.layout.layout_message);
        _messages = messages;
        addAll(messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = _messages.get(position);
        String time = new SimpleDateFormat("hh:MM").format(message.getSendDate());

        ((TextView)convertView.findViewById(R.id.message_username)).setText(message.getSenderUsername());
        ((TextView)convertView.findViewById(R.id.message_body)).setText(message.getBody());
        ((TextView)convertView.findViewById(R.id.message_time)).setText(time);

        return convertView;
    }

    public void addMessage(Message message) {
        _messages.add(message);
        add(message);
    }
}
