package com.example.android_exam.models;

public class Chat {
    private String _name;
    private String _lastMessage;
    private int _nUnreadMessages;


    public Chat(String name, String lastMessage, int nUnreadMessages) {
        _name = name;
        _lastMessage = lastMessage;
        _nUnreadMessages = nUnreadMessages;
    }

    public String getName() {
        return _name;
    }

    public String getLastMessage() {
        return _lastMessage;
    }

    public int getNUnreadMesssages() {
        return _nUnreadMessages;
    }
}
