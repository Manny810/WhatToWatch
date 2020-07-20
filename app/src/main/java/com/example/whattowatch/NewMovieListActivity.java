package com.example.whattowatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.whattowatch.adapters.FindMoviesAdapter;
import com.example.whattowatch.adapters.MovieListAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewMovieListActivity extends AppCompatActivity {

    /**
     * NewMovieListActivity is the activity responsible for creating a new movie list
     * The app goes to this activity when the user is trying to create a new movie list
     */
    public static final String TAG = "NewMovieListActivity";
    Button btnFindMovie;
    Button btnFinishNewList;
    RecyclerView rvMovieList;
    EditText etMovieListName;
    List<Movie> movies;
    MovieListAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_list);

        btnFindMovie = findViewById(R.id.btnFindMovie);
        rvMovieList = findViewById(R.id.rvMovieList);
        btnFinishNewList = findViewById(R.id.btnFinishNewList);
        etMovieListName = findViewById(R.id.etMovieListName);

        movies = new ArrayList<>();

        btnFindMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMovie = new Intent(NewMovieListActivity.this, FindMovieActivity.class);
                startActivityForResult(newMovie, 1);
            }
        });

        btnFinishNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MovieList movieList = MovieList.movieListMaker(ParseUser.getCurrentUser(), etMovieListName.getText().toString(), movies);
                    movieList.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null){
                                Log.e(TAG, "Error while saving", e);
                                Toast.makeText(NewMovieListActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                            }
                            Log.i(TAG, "Post save was successful!");
                            etMovieListName.setText("");
                            movies = new ArrayList<>();
                            movieAdapter.notifyDataSetChanged();
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception Thrown", e);
                }
            }
        });

        // create adapter for RecyclerView
        movieAdapter = new MovieListAdapter(this, movies);

        // set the adapter as the recycler view adapter
        rvMovieList.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvMovieList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            assert data != null;
            Movie movie = (Movie) data.getParcelableExtra(Movie.class.getSimpleName());
            movies.add(movie);
            movieAdapter.notifyDataSetChanged();
        }
    }
}