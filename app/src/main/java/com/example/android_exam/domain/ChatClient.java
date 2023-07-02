package com.example.android_exam.domain;

import android.util.Log;
import android.widget.Toast;

import com.example.android_exam.models.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;

public class ChatClient implements Runnable {
    public enum Cmd {
        SIGN_UP,
        SIGN_IN,
        CONNECT,
        DISCONNECT,
        SEND_MESSAGE,
        GET_MESSAGE,
        GET_CHAT_LIST,
        GET_MESSAGE_LIST,
        GET_USER_LIST,
        QUIT
    }

    public enum Status {
        OK,
        ERROR
    }

    private static ChatClient _instance;
    private static Thread _thread;

    private String _hostAddr;
    private int _port;
    private Stack<ClientEventHandler> _handlers;
    private ChatClientWriter _writer;
    private Thread _writerThread;

    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;
    private boolean _keepRunning = true;
    private User _currentUser = null;


    private ChatClient() { }

    public static ChatClient init(String hostAddr, int port, ClientEventHandler handler) {
        if (_instance != null)
            return _instance;

        _instance = new ChatClient();
        _thread = new Thread(_instance);
        _thread.start();

        _instance._hostAddr = hostAddr;
        _instance._port = port;
        _instance._handlers = new Stack<>();
        _instance._handlers.push(handler);

        _instance._writer = new ChatClientWriter();
        _instance._writerThread = new Thread(_instance._writer);
        _instance._writerThread.start();

        return _instance;
    }

    public static ChatClient getInstance() {
        return _instance;
    }

    public User getCurrentUser() {
        return _currentUser;
    }

    public void setHandler(ClientEventHandler handler) {
        _handlers.push(handler);
    }

    public void resetHandler(ClientEventHandler handler) {
        if (_handlers.peek() == handler)
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
        _writer.enqueue(i -> {
            if (_out == null || _out.checkError()) {
                Log.d("ERROR", "_out == null");
                return;
            }

            _out.write(cmd.name());
            _out.write('\n');

            for (Object param : params) {
                _out.write(param.toString());
                _out.write('\n');
            }

            _out.flush();
        });
    }

    public void signUp(User user) {
        sendCmd(Cmd.SIGN_UP, user.getUsername(), user.getPassword());
    }

    public void signIn(User user) {
        sendCmd(Cmd.SIGN_IN, user.getUsername(), user.getPassword());
        _currentUser = user;
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

    public void getUserList() {
        sendCmd(Cmd.GET_USER_LIST);
    }

    public void quit() {
        sendCmd(Cmd.QUIT);
    }

    private boolean connect() {
        try {
            _socket = new Socket(_hostAddr, _port);
            _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream(), StandardCharsets.UTF_8)), false);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream(), StandardCharsets.UTF_8));

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
                handler.onConnect(ok, new Chat(_in));
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
            case GET_USER_LIST:
                handler.onGetUserList(ok, readList(User::new));
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
