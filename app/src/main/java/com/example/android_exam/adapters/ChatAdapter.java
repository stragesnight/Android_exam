package com.example.android_exam.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.android_exam.R;
import com.example.android_exam.models.Chat;

public class ChatAdapter extends ArrayAdapter<Chat> {
    public ChatAdapter(@NonNull Context context) {
        super(context, R.layout.layout_chat);
    }
}
