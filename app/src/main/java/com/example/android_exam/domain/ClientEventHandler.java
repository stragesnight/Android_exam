package com.example.android_exam.domain;

import com.example.android_exam.models.*;

import java.util.List;

public interface ClientEventHandler {
    default void onSignUp(boolean ok) { }
    default void onSignIn(boolean ok) { }
    default void onConnect(boolean ok) { }
    default void onSendMessage(boolean ok) { }
    default void onDisconnect(boolean ok) { }
    default void onGetChatList(boolean ok, List<Chat> chats) { }
    default void onGetMessageList(boolean ok, List<Message> messages) { }
    default void onGetMessage(boolean ok, Message message) { }
}
