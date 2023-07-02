package com.example.android_exam.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import com.example.android_exam.R;
import com.example.android_exam.activities.CreateChatActivity;
import com.example.android_exam.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private List<User> _users;


    public UserAdapter(@NonNull Context context, List<User> users) {
        super(context, R.layout.layout_user, R.id.user_name);
        _users = users;
        addAll(users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        User user = _users.get(position);
        ((TextView)view.findViewById(R.id.user_name)).setText(user.getUsername());
        view.setOnClickListener(v -> ((CreateChatActivity)getContext()).onUserClick(user));

        return view;
    }

    public void applyFilter(String username) {
        String lowercase = username.toLowerCase();

        clear();
        if (username.isEmpty()) {
            addAll(_users);
            return;
        }

        for (User user : _users) {
            if (user.getUsername().toLowerCase().contains(lowercase))
                add(user);
        }
    }
}
