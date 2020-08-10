package com.example.whattowatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whattowatch.adapters.RecommenderAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    Set<Movie> movieSet;
    RecommenderAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_list);

        btnFindMovie = findViewById(R.id.btnFindMovie);
        rvMovieList = findViewById(R.id.rvMovieList);
        btnFinishNewList = findViewById(R.id.btnFinishNewList);
        etMovieListName = findViewById(R.id.etMovieListName);

        movies = new ArrayList<>();
        movieSet = new HashSet<>();

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
                if (!etMovieListName.getText().toString().equals("")) {
                    try {
                        MovieList movieList = MovieList.movieListMaker(ParseUser.getCurrentUser(), etMovieListName.getText().toString(), movies);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(MovieList.class.getSimpleName(), Parcels.wrap(movieList));
                        setResult(RESULT_OK, returnIntent);

                        movieList.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
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
                } else {
                    Toast.makeText(NewMovieListActivity.this, "You must title your movie list!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // create adapter for RecyclerView
        movieAdapter = new RecommenderAdapter(this, movies);

        // set the adapter as the recycler view adapter
        rvMovieList.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvMovieList.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbNewMovieList);
        setSupportActionBar(toolbar);
    }

    /**
     *  We are overwriting onActivityResult so when we get back, we add the selected movie in the previous activity to our recyclerView
     *  and then notify the recyclerView adapter that the recyclerView changed and must be updated
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            Movie movie = (Movie) Parcels.unwrap(data.getParcelableExtra(Movie.class.getSimpleName()));
            if (movieSet.contains(movie)){
                Toast.makeText(NewMovieListActivity.this, "Movie already is in the Movie List!", Toast.LENGTH_SHORT).show();
            } else {
                movies.add(movie);
                movieSet.add(movie);
                movieAdapter.notifyDataSetChanged();
            }
        }
    }
}