package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FindMovieActivity extends AppCompatActivity {
    /**
     * This activity is responsible for finding a movie that the user wants to add to a list they are making
     * The app comes to this activity when the user is trying to make a new movie list and they are trying to find the movie to add
     * to the list. 
     */
    public static final String TAG = "FindMovieActivity";

    EditText etMovieSearch;
    RecyclerView rvMovieSearch;
    Button btnMovieSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        etMovieSearch = findViewById(R.id.etMovieSearch);
        rvMovieSearch = findViewById(R.id.rvMovieSearch);
        btnMovieSearch = findViewById(R.id.btnMovieSearch);
    }
}