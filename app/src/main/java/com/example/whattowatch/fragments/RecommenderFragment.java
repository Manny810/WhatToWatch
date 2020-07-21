package com.example.whattowatch.fragments;

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

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowatch.R;
import com.example.whattowatch.adapters.MovieListAdapter;
import com.example.whattowatch.adapters.RecommenderAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;


public class RecommenderFragment extends Fragment {
    public static final String TAG = "RecommenderFragment";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=557bf444fa647aa33e0e1a2de0317f55&language=en-US&page=1";


    RecyclerView rvRecommendation;

    RecommenderAdapter movieAdapter;
    List<Movie> movieRecommendations;
    Set<Movie> movies;

    AsyncHttpClient client = new AsyncHttpClient();

    public RecommenderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRecommendation = view.findViewById(R.id.rvRecommendation);

        movieRecommendations = new ArrayList<>();
        movies = new HashSet<>();

        movieAdapter = new RecommenderAdapter(getContext(), movieRecommendations);

        rvRecommendation.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvRecommendation.setLayoutManager(new LinearLayoutManager(getContext()));

        queryRecommendations();
    }


    protected void queryRecommendations() {
        // Specify which class to query
        final Set<Movie> movieRecs = new HashSet<>();
        ParseQuery<MovieList> query = ParseQuery.getQuery(MovieList.class);
        query.include(MovieList.KEY_USER);
        query.whereEqualTo(MovieList.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(MovieList.KEY_CREATED_AT);

        // get set of all movies
        query.findInBackground(new FindCallback<MovieList>() {
            @Override
            public void done(List<MovieList> newMovieLists, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (MovieList movieList: newMovieLists){
                    try {
                        movies.addAll(movieList.getSetOfMovies());
                    } catch (JSONException ex) {
                        Log.e(TAG, "Json Exception thrown", ex);
                    }
                }

                for (Movie movie: movies){
                    client.get(String.format(NOW_PLAYING_URL, movie.getID()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess");
                            JSONObject jsonObject = json.jsonObject;

                            try {
                                JSONArray results = jsonObject.getJSONArray("results");
                                Movie movie = Movie.fromJsonObject(results.getJSONObject(0));
                                if (!movieRecs.contains(movie)) {
                                    movieRecs.add(Movie.fromJsonObject(results.getJSONObject(0)));
                                    movieRecommendations.add(Movie.fromJsonObject(results.getJSONObject(0)));
                                    movieAdapter.notifyDataSetChanged();
                                }
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
        });
    }
}