package com.example.android_exam.domain;

import android.util.Log;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChatClientWriter implements Runnable {
    private boolean _keepRunning = true;
    private Queue<Consumer<Integer>> _queue = new ArrayDeque<>(10);


    @Override
    public void run() {
        while (_keepRunning) {
            try {
                if (_queue.isEmpty())
                    TimeUnit.MILLISECONDS.sleep(100);
                else
                    _queue.poll().accept(0);
            } catch (Exception e) {
                Log.d("EXCEPTION", e.getMessage());
            }
        }
    }

    public void enqueue(Consumer<Integer> consumer) {
        _queue.add(consumer);
    }
}
