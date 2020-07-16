package com.example.whattowatch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whattowatch.CameraActivity;
import com.example.whattowatch.NewMovieListActivity;
import com.example.whattowatch.R;
import com.parse.ParseUser;


public class MovieListFragment extends Fragment {

    RecyclerView rvMovieLists;
    Button btnNewList;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();

        btnNewList = view.findViewById(R.id.btnNewList);

        btnNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieListIntent = new Intent(getContext(), NewMovieListActivity.class);
                startActivity(movieListIntent);
            }
        });

    }
}