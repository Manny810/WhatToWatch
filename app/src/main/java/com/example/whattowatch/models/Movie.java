package com.example.whattowatch.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ParseClassName("Movie")
public class Movie extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "posterPath";
    public static final String KEY_BACKDROP_PATH = "backdropPath";
    public static final String KEY_ID = "id";

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(fromJsonObject(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public static Movie fromJsonObject(JSONObject movie) throws JSONException {
        Movie new_movie = new Movie();
        new_movie.setDescription(movie.getString(KEY_DESCRIPTION));
        new_movie.setTitle(movie.getString(KEY_TITLE));
        new_movie.setPosterPath(movie.getString(KEY_POSTER_PATH));
        new_movie.setBackdropPath(movie.getString(KEY_BACKDROP_PATH));
        new_movie.setID(movie.getInt(KEY_ID));

        return new_movie;
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public String getTitle(){
        return getString(KEY_TITLE);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public String getPosterPath(){
        return getString(KEY_POSTER_PATH);
    }

    public void setPosterPath(String posterPath){
        put(KEY_POSTER_PATH, posterPath);
    }

    public String getBackdropPath(){
        return getString(KEY_BACKDROP_PATH);
    }

    public void setBackdropPath(String backdropPath){
        put(KEY_BACKDROP_PATH, backdropPath);
    }

    public Number getID(){
        return getNumber(KEY_ID);
    }

    public void setID(Number id){
        put(KEY_ID, id);
    }
}
