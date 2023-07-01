package com.example.android_exam.domain;

import android.util.Log;

import com.example.android_exam.models.User;

import java.io.*;

public class StorageManager {
    public boolean saveAuthData(User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(".auth"));
            writer.write(user.toString());
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
        }
    }

    public User loadAuthData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(".auth"));
            User user = new User(reader);
            reader.close();
            return user;
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return null;
        }
    }
}
