package com.example.android_exam.models;

import android.util.Log;

import java.io.*;

public class Chat {
    private String _name;
    private String _lastMessage;


    public Chat(String name, String lastMessage) {
        _name = name;
        _lastMessage = lastMessage;
    }

    public Chat(BufferedReader reader) {
        try {
            _name = reader.readLine();
            _lastMessage = reader.readLine();
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public String getName() {
        return _name;
    }

    public String getLastMessage() {
        return _lastMessage;
    }
}
