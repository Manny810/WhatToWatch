package com.example.whattowatch.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Movie")
public class Movie extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "posterPath";
    public static final String KEY_BACKDROP_PATH = "backdropPath";
    public static final String KEY_ID = "id";

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
