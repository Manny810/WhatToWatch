package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowatch.adapters.MovieAdapter;
import com.example.whattowatch.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class FindMovieActivity extends AppCompatActivity {
    /**
     * This activity is responsible for finding a movie that the user wants to add to a list they are making
     * The app comes to this activity when the user is trying to make a new movie list and they are trying to find the movie to add
     * to the list.
     */
    public static final String TAG = "FindMovieActivity";

    public static final String MOVIE_SEARCH_API_URL = "https://api.themoviedb.org/3/search/movie?api_key=557bf444fa647aa33e0e1a2de0317f55&language=en-US&query=%s&page=1&include_adult=false";

    EditText etMovieSearch;
    RecyclerView rvMovieSearch;
    Button btnMovieSearch;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        etMovieSearch = findViewById(R.id.etMovieSearch);
        rvMovieSearch = findViewById(R.id.rvMovieSearch);
        btnMovieSearch = findViewById(R.id.btnMovieSearch);
        movies = new ArrayList<>();

        // create adapter for RecyclerView
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // set the adapter as the recycler view adapter
        rvMovieSearch.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvMovieSearch.setLayoutManager(new LinearLayoutManager(this));

        final AsyncHttpClient client = new AsyncHttpClient();

        btnMovieSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String api_url = String.format(MOVIE_SEARCH_API_URL, etMovieSearch.getText());
                client.get(api_url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            movies.clear();
                            movies.addAll(Movie.fromJsonArray(results));
                            movieAdapter.notifyDataSetChanged();
                            Log.i(TAG, "movies displayed: " + movies.size());

                        } catch (JSONException e) {
                            Log.e(TAG, "Hit JSON exception", e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure called on Async client");
                    }
                });

            }
        });
    }
}