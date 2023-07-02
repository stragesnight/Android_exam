package com.example.android_exam.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;

import com.example.android_exam.domain.ChatClient;
import com.example.android_exam.models.Message;
import com.example.android_exam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private List<Message> _messages = null;


    public MessageAdapter(@NonNull Context context) {
        super(context, R.layout.layout_message, R.id.message_username);
    }

    public void setMessages(List<Message> messages) {
        _messages = new ArrayList<>(messages);
        addAll(messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Message message = _messages.get(position);
        String time = new SimpleDateFormat("hh:mm:ss").format(message.getSendDate());

        ((TextView)view.findViewById(R.id.message_username)).setText(message.getSenderUsername());
        ((TextView)view.findViewById(R.id.message_body)).setText(message.getBody());
        ((TextView)view.findViewById(R.id.message_time)).setText(time);

        if (message.getSenderUsername().compareTo(ChatClient.getInstance().getCurrentUser().getUsername()) == 0) {
            view.findViewById(R.id.layout_message_main).setBackground(
                    getContext().getDrawable(R.drawable.rounded_card_self));
        } else {
            ((ViewGroup)view).removeView(view.findViewById(R.id.message_padder));
            view.findViewById(R.id.layout_message_main).setBackground(
                    getContext().getDrawable(R.drawable.rounded_card));
        }

        return view;
    }

    public void addMessage(Message message) {
        if (_messages == null)
            _messages = new ArrayList<>();
        _messages.add(message);
        add(message);
    }
}
