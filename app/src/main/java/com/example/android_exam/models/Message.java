package com.example.android_exam.models;

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
