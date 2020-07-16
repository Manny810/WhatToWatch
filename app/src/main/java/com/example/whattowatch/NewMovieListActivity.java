package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.whattowatch.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class NewMovieListActivity extends AppCompatActivity {

    public static final String TAG = "NewMovieListActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_list);

        movies = new ArrayList<Movie>();

    }
}