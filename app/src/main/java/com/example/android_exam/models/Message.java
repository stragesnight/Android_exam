package com.example.android_exam.models;

import android.util.Log;

import java.io.*;
import java.text.*;
import java.util.Date;

public class Message {
    private String _senderUsername;
    private String _body;
    private Date _sendDate;


    public Message(String username, String body, Date sendDate) {
        _senderUsername = username;
        _body = body;
        _sendDate = sendDate;
    }

    public Message(BufferedReader reader) {
        try {
            _senderUsername = reader.readLine();
            _body = reader.readLine().replace("\\n", "\n");
            _sendDate = new SimpleDateFormat("hh:mm:ss").parse(reader.readLine());
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public String getSenderUsername() {
        return _senderUsername;
    }

    public String getBody() {
        return _body;
    }

    public Date getSendDate() {
        return _sendDate;
    }
}
