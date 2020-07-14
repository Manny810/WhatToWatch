package com.example.whattowatch.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.lang.reflect.Array;

@ParseClassName("MovieList")
public class MovieList extends ParseObject {

    public static final String KEY_MOVIES = "movies";
    public static final String KEY_TITLE = "title";
    public static final String KEY_USER = "user";


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
}
