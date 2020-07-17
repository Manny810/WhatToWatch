package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.whattowatch.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class NewMovieListActivity extends AppCompatActivity {

    /**
     * NewMovieListActivity is the activity responsible for creating a new movie list
     * The app goes to this activity when the user is trying to create a new movie list
     */
    public static final String TAG = "NewMovieListActivity";
    List<Movie> movies;

    Button btnFindMovie;
    RecyclerView rvMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_list);

        movies = new ArrayList<Movie>();
        btnFindMovie = findViewById(R.id.btnFindMovie);
        rvMovieList = findViewById(R.id.rvMovieList);

    }
}