package com.example.android_exam.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.android_exam.models.Message;
import com.example.android_exam.R;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context) {
        super(context, R.layout.layout_message);
    }
}
