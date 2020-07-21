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

    /**
     * fromJsonArray is a movie producer that produces a list a movies from a JSONArray.
     * The JSONArray must be from MovieDatabaseAPI, and must be an array of JSONObjects that represent movies
     */
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(fromJsonObject(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    /**
     *
     * @param movie a JSONObject from MovieDatabaseApi that represents a movie
     * @return an instance of Movie
     * @throws JSONException if the object is not from MovieDatabaseApi as it won't have the right keywords
     */
    public static Movie fromJsonObject(JSONObject movie) throws JSONException {
        Movie new_movie = new Movie();
        new_movie.setDescription(movie.getString("overview"));
        new_movie.setTitle(movie.getString("title"));
        new_movie.setPosterPath(movie.getString("poster_path"));
        new_movie.setBackdropPath(movie.getString("backdrop_path"));
        new_movie.setID(movie.getInt("id"));

        return new_movie;
    }

    /**
     *
     * @return a JSONObject representation of movie
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_DESCRIPTION, getDescription());
        json.put(KEY_TITLE, getTitle());
        json.put(KEY_POSTER_PATH, getPosterPath());
        json.put(KEY_BACKDROP_PATH, getBackdropPath());
        json.put(KEY_ID, getID());
        return json;
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

    public String getPosterPath(){ return String.format("https://image.tmdb.org/t/p/w342/%s", getString(KEY_POSTER_PATH)); }

    public void setPosterPath(String posterPath){
        put(KEY_POSTER_PATH, posterPath);
    }

    public String getBackdropPath(){ return String.format("https://image.tmdb.org/t/p/w342/%s", getString(KEY_BACKDROP_PATH)); }

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
