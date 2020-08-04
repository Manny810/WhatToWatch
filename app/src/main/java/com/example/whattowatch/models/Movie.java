package com.example.whattowatch.models;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Parcel
public class Movie {

    public Movie() {}

    String description;
    String title;
    String posterPath;
    String backdropPath;
    Integer id;
    Double voteAverage;

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "posterPath";
    public static final String KEY_BACKDROP_PATH = "backdropPath";
    public static final String KEY_ID = "id";
    public static final String KEY_VOTE_AVERAGE = "voteAverage";

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Movie){
            Movie that = (Movie) obj;
            return this.getDescription().equals(that.getDescription()) && this.getTitle().equals(that.getTitle())
                    && this.getPosterPath().equals(that.getPosterPath()) && this.getBackdropPath().equals(that.getBackdropPath())
                    && this.getID().equals(that.getID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getDescription().hashCode() + this.getPosterPath().hashCode() + this.getBackdropPath().hashCode() + this.getID().hashCode() + this.getTitle().hashCode();
    }

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
        new_movie.setVoteAverage(movie.getDouble("vote_average"));


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
        json.put(KEY_POSTER_PATH, getPosterPathKey());
        json.put(KEY_BACKDROP_PATH, getBackdropPathKey());
        json.put(KEY_ID, getID());
        json.put(KEY_VOTE_AVERAGE, getVoteAverage());
        return json;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getPosterPath(){ return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath); }

    public String getPosterPathKey(){ return posterPath; }

    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

    public String getBackdropPath(){ return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath); }

    public String getBackdropPathKey(){ return backdropPath; }

    public void setBackdropPath(String backdropPath){
        this.backdropPath = backdropPath;
    }

    public Integer getID(){
        return id;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public Double getVoteAverage() { return voteAverage; }

    public void setVoteAverage(Double voteAverage) { this.voteAverage = voteAverage; }

}
