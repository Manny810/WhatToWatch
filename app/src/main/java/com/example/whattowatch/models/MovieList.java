package com.example.whattowatch.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ParseClassName("MovieList")
public class MovieList extends ParseObject {

    public static final String KEY_MOVIES = "movies";
    public static final String KEY_TITLE = "title";
    public static final String KEY_USER = "user";

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

            movies.add(newMovie);
        }

        return movies;
    }
}
