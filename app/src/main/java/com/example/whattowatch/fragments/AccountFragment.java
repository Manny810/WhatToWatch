package com.example.whattowatch.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.whattowatch.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    public static final String TAG = "AccountFragment";
    private EditText etUsername;
    private ImageView ivProfilePhoto;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();

        etUsername = view.findViewById(R.id.etUsername);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);

        etUsername.setText(user.getUsername());


    }
}