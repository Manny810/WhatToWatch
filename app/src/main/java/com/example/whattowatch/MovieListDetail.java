package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowatch.adapters.RecommenderAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

public class MovieListDetail extends AppCompatActivity {
    public static final String TAG = "MovieListDetail";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=557bf444fa647aa33e0e1a2de0317f55&language=en-US&page=1";
    public static final String RECOMMENDATION_TEXT = "Your Recommendations";
    public static final String MOVIE_LIST_TEXT = "Movies in Your List";
    public static final String MOVIE_LIST_BUTTON_TEXT = "Switch to Movies in Your List";
    public static final String RECOMMENDATION_BUTTON_TEXT = "Switch to Recommendations";

    TextView tvListTitle;
    TextView tvListSize;
    TextView tvListCreatedAt;
    TextView tvRecommendationSize;
    TextView tvRecyclerViewTitle;
    Button btnSwitchList;

    Boolean onRecommendations = true;

    RecyclerView rvMovieListDetail;
    RecyclerView rvMovieListRecommendations;

    MovieList movieList;
    List<Movie> movies;
    List<Movie> movieRecs;
    Set<Movie> moviesSet;

    RecommenderAdapter movieAdapter;
    RecommenderAdapter movieRecAdapter;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_detail);

        tvListTitle = findViewById(R.id.tvListTitle);
        tvListSize = findViewById(R.id.tvListSize);
        tvListCreatedAt = findViewById(R.id.tvListCreatedAt);
        tvRecommendationSize = findViewById(R.id.tvRecommendationSize);
        rvMovieListDetail = findViewById(R.id.rvMovieListDetail);
        rvMovieListRecommendations = findViewById(R.id.rvMovieListRecommendations);
        rvMovieListDetail.setVisibility(View.INVISIBLE);
        btnSwitchList = findViewById(R.id.btnSwitchList);
        tvRecyclerViewTitle = findViewById(R.id.tvRecyclerViewTitle);

        // unwrap the movieList passed in via intent, using its simple name as a key
        movieList = (MovieList) Parcels.unwrap(getIntent().getParcelableExtra(MovieList.class.getSimpleName()));

        assert movieList != null;
        movies = movieList.getListOfMovies();
        try {
            moviesSet = movieList.getSetOfMovies();
        } catch (JSONException e) {
            Log.e(TAG, "Json exception", e);
        }
        movieRecs = new ArrayList<>();
        movieAdapter = new RecommenderAdapter(this, movies);
        movieRecAdapter = new RecommenderAdapter(this, movieRecs);
        rvMovieListDetail.setAdapter(movieAdapter);
        rvMovieListRecommendations.setAdapter(movieRecAdapter);
        rvMovieListDetail.setLayoutManager(new LinearLayoutManager(this));
        rvMovieListRecommendations.setLayoutManager(new LinearLayoutManager(this));

        tvListTitle.setText(movieList.getTitle());
        tvListSize.setText(String.format("Number of Movies: %d", movies.size()));
        tvListCreatedAt.setText(String.format("Created %s", getRelativeTimeAgo(movieList.getCreatedAt().toString())));
        tvRecyclerViewTitle.setText(RECOMMENDATION_TEXT);

        btnSwitchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecommendations){
                    // switch recycler views to movieList movies
                    onRecommendations = false;
                    tvRecyclerViewTitle.setText(MOVIE_LIST_TEXT);
                    btnSwitchList.setText(RECOMMENDATION_BUTTON_TEXT);
                    rvMovieListRecommendations.setVisibility(View.INVISIBLE);
                    rvMovieListDetail.setVisibility(View.VISIBLE);

                } else {
                    // switch recycler views to movieList Recommendations
                    onRecommendations = true;
                    tvRecyclerViewTitle.setText(RECOMMENDATION_TEXT);
                    btnSwitchList.setText(MOVIE_LIST_BUTTON_TEXT);
                    rvMovieListDetail.setVisibility(View.INVISIBLE);
                    rvMovieListRecommendations.setVisibility(View.VISIBLE);

                }
            }
        });

        getRecommendations();

    }

    private void getRecommendations() {
        final Map<Movie, Double> recommendationScores = new HashMap<>();
        Set<Movie> movieSetInList = new HashSet<>();
        for (Movie movie: movies){
            movieSetInList.add(movie);
        }
        for (Movie movie : movies) {
            client.get(String.format(NOW_PLAYING_URL, movie.getID()), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess");
                    JSONObject jsonObject = json.jsonObject;

                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                        for (int i = 0; i < 20; i++){
                            Movie movie = Movie.fromJsonObject(results.getJSONObject(i));
                            if (!moviesSet.contains(movie)) { // checking to see if the recommended movie is already in our movieList so we don't add it
                                if (recommendationScores.containsKey(movie)) {
                                    // if we have seen this movie before, change the score
                                    Double newScore = recommendationScores.get(movie) + 1 - .02 * i;
                                    recommendationScores.put(movie, newScore);
                                } else {
                                    recommendationScores.put(movie, 1 - .02 * i);
                                }
                            }
                        }

                        Comparator<Map.Entry<Movie, Double>> valueComparator = new Comparator<Map.Entry<Movie,Double>>() {
                            @Override public int compare(Map.Entry<Movie, Double> e1, Map.Entry<Movie, Double> e2) {
                                Double v1 = e1.getValue(); Double v2 = e2.getValue(); return v2.compareTo(v1);
                            }
                        };

                        // Sort method needs a List, so let's first convert Set to List in Java
                        List<Map.Entry<Movie, Double>> listOfEntries = new ArrayList<Map.Entry<Movie, Double>>(recommendationScores.entrySet()); // sorting HashMap by values using comparator Collections.sort(listOfEntries, valueComparator);
                        Collections.sort(listOfEntries, valueComparator);
                        movieRecs.clear();
                        for (Map.Entry<Movie, Double> entry: listOfEntries){
                            Log.d(TAG, "Movie: " + entry.getKey().getTitle() + ", Value: " + entry.getValue());
                            movieRecs.add(entry.getKey());
                        }
                        movieRecAdapter.notifyDataSetChanged();

                    } catch (JSONException ex) {
                        Log.e(TAG, "Hit Json Exception", ex);
                    }

                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure called");
                }
            });
        }

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}