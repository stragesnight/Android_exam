package com.example.android_exam.domain;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.android_exam.models.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private boolean _connected = false;
    private Handler _mainHandler = null;


    private static ChatClient init(String hostAddr, int port) {
        if (_instance != null)
            return _instance;

        _instance = new ChatClient();
        _thread = new Thread(_instance);
        _thread.start();

        _instance._hostAddr = hostAddr;
        _instance._port = port;
        _instance._handlers = new Stack<>();
        _instance._mainHandler = new Handler(Looper.getMainLooper());

        _instance._writer = new ChatClientWriter();
        _instance._writerThread = new Thread(_instance._writer);
        _instance._writerThread.start();

        return _instance;
    }

    public static ChatClient getInstance() {
        if (_instance == null)
            _instance = ChatClient.init("178.54.217.55", 4444);
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
        if (!_connected && !connect()) {
            Log.d("ERROR", "Unable to connect to server");
            return;
        }

        while (_keepRunning) {
            try {
                if (!_in.ready())
                    TimeUnit.MILLISECONDS.sleep(100);

                Cmd cmd = Cmd.valueOf(_in.readLine());
                boolean ok = _in.readLine().compareTo(Status.OK.name()) == 0;
                handleCmd(ok, cmd);
            } catch (Exception e) {
                Log.d("run_EXCEPTION", e.getMessage());
            }
        }

        try {
            _in.close();
            _out.close();
            _socket.close();
        } catch (Exception e) {
            Log.d("run_EXCEPTION", e.getMessage());
        }
    }

    public void sendCmd(Cmd cmd, Object... params) {
        _writer.enqueue(i -> {
            if (!_connected)
                connect();

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
            _connected = true;

            return true;
        } catch (IOException e) {
            Log.d("connect_EXCEPTION", e.getMessage());
            return false;
        }
    }

    private void handleCmd(boolean ok, Cmd cmd) throws IOException {
        if (_handlers.empty())
            return;

        ClientEventHandler handler = _handlers.peek();

        switch (cmd) {
            case SIGN_UP:
                _mainHandler.post(() -> handler.onSignUp(ok));
                break;
            case SIGN_IN:
                _mainHandler.post(() -> handler.onSignIn(ok));
                break;
            case CONNECT:
                Chat chat = ok ? new Chat(_in) : null;
                _mainHandler.post(() -> handler.onConnect(ok, chat));
                break;
            case DISCONNECT:
                _mainHandler.post(() -> handler.onDisconnect(ok));
                break;
            case SEND_MESSAGE:
                _mainHandler.post(() -> handler.onSendMessage(ok));
                break;
            case GET_MESSAGE:
                Message msg = ok ? new Message(_in) : null;
                _mainHandler.post(() -> handler.onGetMessage(ok, msg));
                break;
            case GET_CHAT_LIST:
                List<Chat> chats = ok ? readList(Chat::new) : null;
                _mainHandler.post(() -> handler.onGetChatList(ok, chats));
                break;
            case GET_MESSAGE_LIST:
                List<Message> messages = ok ? readList(Message::new) : null;
                _mainHandler.post(() -> handler.onGetMessageList(ok, messages));
                break;
            case GET_USER_LIST:
                List<User> users = ok ? readList(User::new) : null;
                _mainHandler.post(() -> handler.onGetUserList(ok, users));
                break;
            default:
                break;
        }
    }

    private <T> List<T> readList(Function<BufferedReader, T> converter) throws IOException {
        int length = Integer.parseInt(_in.readLine());
        List<T> res = new ArrayList<>(length);

        for (; length > 0; --length)
            res.add(converter.apply(_in));

        return res;
    }
}
