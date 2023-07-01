package com.example.android_exam.models;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class User {
    private String _username;
    private String _password;


    public User(String username, String password) {
        _username = username;
        _password = password;
    }

    public User(BufferedReader reader) {
        try {
            _username = reader.readLine();
            _password = reader.readLine();
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }
}
