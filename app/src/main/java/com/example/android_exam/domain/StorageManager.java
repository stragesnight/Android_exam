package com.example.android_exam.domain;

import android.content.Context;
import android.util.Log;

import com.example.android_exam.models.User;

import java.io.*;

public class StorageManager {
    private Context _context;


    public StorageManager(Context context) {
        _context = context;
    }

    public boolean saveAuthData(User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    _context.getFilesDir().getAbsolutePath() + "/auth"));
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
            BufferedReader reader = new BufferedReader(new FileReader(
                    _context.getFilesDir().getAbsolutePath() + "/auth"));
            User user = new User(reader);
            reader.close();
            return user;
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return null;
        }
    }
}
