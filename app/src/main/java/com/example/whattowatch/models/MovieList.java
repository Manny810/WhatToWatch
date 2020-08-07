package com.example.whattowatch.models;


import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

@ParseClassName("MovieList")
public class MovieList extends ParseObject {
    public static final String TAG = "MovieList";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=557bf444fa647aa33e0e1a2de0317f55&language=en-US&page=1";


    public static final String KEY_MOVIES = "movies";
    public static final String KEY_TITLE = "title";
    public static final String KEY_USER = "user";

    /**
     * movieListMaker is a function that transforms a list of movies into a parse instance of MovieList
     * @param user the ParseUser owner of the Movie List being made, typically will be ParseUser.getCurrentOwner()
     * @param title the title of the Movie List
     * @param movies The list of movies that will be stored in the list
     * @return a parse instance of MovieList
     * @throws JSONException
     */
    public static MovieList movieListMaker(ParseUser user, String title, List<Movie> movies) throws JSONException {
        MovieList movieList = new MovieList();
        movieList.setUser(user);
        movieList.setTitle(title);
        JSONArray jsonArray = new JSONArray();
        for (Movie movie: movies){
            jsonArray.put(movie.toJson());
        }
        movieList.setMovies(jsonArray);

        return movieList;
    }

    public JSONArray getMovies(){
        return getJSONArray(KEY_MOVIES);
    }

    public void setMovies(JSONArray movies){
        put(KEY_MOVIES, movies);
    }

    public String getTitle(){
        return getString(KEY_TITLE);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    /**
     *
     * @return a set of movie instances for all movies within the movie list
     * @throws JSONException
     */
    public Set<Movie> getSetOfMovies() throws JSONException {
        Set<Movie> movies = new HashSet<>();
        JSONArray jsonArray = getMovies();

        for (int i = 0; i < jsonArray.length(); i++){
            Movie newMovie = new Movie();
            newMovie.setDescription(jsonArray.getJSONObject(i).getString(Movie.KEY_DESCRIPTION));
            newMovie.setTitle(jsonArray.getJSONObject(i).getString(Movie.KEY_TITLE));
            newMovie.setPosterPath(jsonArray.getJSONObject(i).getString(Movie.KEY_POSTER_PATH));
            newMovie.setBackdropPath(jsonArray.getJSONObject(i).getString(Movie.KEY_BACKDROP_PATH));
            newMovie.setID(jsonArray.getJSONObject(i).getInt(Movie.KEY_ID));
            newMovie.setVoteAverage(jsonArray.getJSONObject(i).getDouble(Movie.KEY_VOTE_AVERAGE));

            movies.add(newMovie);
        }

        return movies;
    }

    public List<Movie> getListOfMovies() {
        List<Movie> movies = new ArrayList<>();
        Set<Movie> movieSet = null;
        try {
            movieSet = getSetOfMovies();
        } catch (JSONException e) {
            movieSet = new HashSet<>();
        }
        for (Movie movie: movieSet){
            movies.add(movie);
        }
        return movies;
    }

    public static void getNewMovieListRecommendations(final RecyclerView.Adapter adapter, final List<Movie> recyclerViewList, final Set<Movie> movies) throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();

        final Map<Movie, Double> recommendationScores = new HashMap<>();


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
                            if (!movies.contains(movie)) { // checking to see if the recommended movie is already in our movieList so we don't add it
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
                        recyclerViewList.clear();
                        adapter.notifyDataSetChanged();

                        for (Map.Entry<Movie, Double> entry: listOfEntries){
                            Log.d(TAG, "Movie: " + entry.getKey().getTitle() + ", Value: " + entry.getValue());
                            recyclerViewList.add(entry.getKey());
                            adapter.notifyDataSetChanged();

                        }
                        adapter.notifyDataSetChanged();

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
}
