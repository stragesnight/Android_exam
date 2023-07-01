package com.example.android_exam.domain;

import android.util.Log;

import com.example.android_exam.models.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Function;

public class ChatClient implements Runnable {
    public enum Cmd {
        SIGN_UP,
        SIGN_IN,
        CONNECT,
        DISCONNECT,
        SEND_MESSAGE,
        GET_MESSAGE,
        GET_CHAT_LIST,
        GET_MESSAGE_LIST
    }

    public enum Status {
        OK,
        ERROR
    }

    private static ChatClient _instance;

    private String _hostAddr;
    private int _port;
    private Stack<ClientEventHandler> _handlers;

    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;
    private boolean _keepRunning = true;


    public ChatClient(String hostAddr, int port, ClientEventHandler handler) {
        if (_instance != null)
            return;

        _instance = this;

        _instance._hostAddr = hostAddr;
        _instance._port = port;
        _instance._handlers = new Stack<>();
        _instance._handlers.push(handler);
    }

    public static ChatClient getInstance() {
        return _instance;
    }

    public void setHandler(ClientEventHandler handler) {
        _handlers.push(handler);
    }

    public void resetHandler() {
        _handlers.pop();
    }

    @Override
    public void run() {
        if (!connect()) {
            Log.d("ERROR", "Unable to connect to server");
            return;
        }

        while (_keepRunning) {
            try {
                Cmd cmd = Cmd.valueOf(_in.readLine());
                boolean ok = _in.readLine().compareTo(Status.OK.name()) == 0;
                handleCmd(ok, cmd);
            } catch (Exception e) {
                Log.d("EXCEPTION", e.getMessage());
            }
        }

        try {
            _in.close();
            _out.close();
            _socket.close();
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public void sendCmd(Cmd cmd, Object... params) {
        _out.write(cmd.name());
        _out.write('\n');

        for (Object param : params) {
            _out.write(param.toString());
            _out.write('\n');
        }

        _out.flush();
    }

    public void signUp(User user) {
        sendCmd(Cmd.SIGN_UP, user.getUsername(), user.getPassword());
    }

    public void signIn(User user) {
        sendCmd(Cmd.SIGN_IN, user.getUsername(), user.getPassword());
    }

    public void connect(String chatName) {
        sendCmd(Cmd.CONNECT, chatName);
    }

    public void disconnect() {
        sendCmd(Cmd.DISCONNECT);
    }

    public void sendMessage(String messageBody) {
        sendCmd(Cmd.SEND_MESSAGE, messageBody);
    }

    public void getChatList() {
        sendCmd(Cmd.GET_CHAT_LIST);
    }

    public void getMessageList() {
        sendCmd(Cmd.GET_MESSAGE_LIST);
    }

    private boolean connect() {
        try {
            _socket = new Socket(_hostAddr, _port);
            _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream())), false);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            return true;
        } catch (IOException e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
        }
    }

    private void handleCmd(boolean ok, Cmd cmd) throws IOException {
        if (_handlers.empty())
            return;

        ClientEventHandler handler = _handlers.pop();

        switch (cmd) {
            case SIGN_UP:
                handler.onSignUp(ok);
                break;
            case SIGN_IN:
                handler.onSignIn(ok);
                break;
            case CONNECT:
                handler.onConnect(ok);
                break;
            case DISCONNECT:
                handler.onDisconnect(ok);
                break;
            case SEND_MESSAGE:
                handler.onSendMessage(ok);
                break;
            case GET_MESSAGE:
                handler.onGetMessage(ok, new Message(_in));
                break;
            case GET_CHAT_LIST:
                handler.onGetChatList(ok, readList(Chat::new));
                break;
            case GET_MESSAGE_LIST:
                handler.onGetMessageList(ok, readList(Message::new));
                break;
            default:
                break;
        }
    }

    private <T> List<T> readList(Function<BufferedReader, T> converter) throws IOException {
        int length = Integer.parseInt(_in.readLine());
        List<T> res = new ArrayList<>(length);

        for (; length >= 0; --length)
            res.add(converter.apply(_in));

        return res;
    }
}
