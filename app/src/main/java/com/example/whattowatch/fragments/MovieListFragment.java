package com.example.whattowatch.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whattowatch.CameraActivity;
import com.example.whattowatch.NewMovieListActivity;
import com.example.whattowatch.R;
import com.example.whattowatch.adapters.MovieListAdapter;
import com.example.whattowatch.adapters.NewMovieListAdapter;
import com.example.whattowatch.models.MovieList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment {
    public static final String TAG = "MovieListFragment";

    RecyclerView rvMovieLists;
    Button btnNewList;

    MovieListAdapter movieAdapter;
    List<MovieList> movieLists;

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
        rvMovieLists = view.findViewById(R.id.rvMovieLists);

        btnNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieListIntent = new Intent(getContext(), NewMovieListActivity.class);
                startActivity(movieListIntent);
            }
        });

        movieLists = new ArrayList<>();

        // create adapter for RecyclerView
        movieAdapter = new MovieListAdapter(getContext(), movieLists);

        // set the adapter as the recycler view adapter
        rvMovieLists.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvMovieLists.setLayoutManager(new LinearLayoutManager(getContext()));

        queryLists();

    }

    protected void queryLists() {
        // Specify which class to query
        ParseQuery<MovieList> query = ParseQuery.getQuery(MovieList.class);
        query.include(MovieList.KEY_USER);
        query.whereEqualTo(MovieList.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(MovieList.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<MovieList>() {
            @Override
            public void done(List<MovieList> newMovieLists, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (MovieList movieList: newMovieLists){
                    Log.i(TAG, "MovieList: " + movieList.getTitle() + ", username: " + movieList.getUser().getUsername());
                }
                movieLists.addAll(newMovieLists);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }
}