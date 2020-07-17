package com.example.whattowatch.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whattowatch.CameraActivity;
import com.example.whattowatch.LoginActivity;
import com.example.whattowatch.R;
import com.example.whattowatch.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    public static final String TAG = "AccountFragment";
    private TextView tvUsername;
    private ImageView ivProfilePhoto;
    private Button btnSignOut;
    public static final String USER_KEY_PROFILE_PHOTO = "profilePhoto";

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

        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        tvUsername.setText(user.getUsername());
        ParseFile photo = user.getParseFile(USER_KEY_PROFILE_PHOTO);
        if (photo != null){
            Log.d(TAG, "loading profile Photo");
            Glide.with(getContext()).load(photo.getUrl()).into(ivProfilePhoto);

        }
        else{
            Log.d(TAG, "Profile photo not provided");
        }
        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(getContext(), CameraActivity.class);
                startActivity(cameraIntent);

            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                //getActivity().startService(intent);
                getActivity().finish();
            }
        });

    }


}