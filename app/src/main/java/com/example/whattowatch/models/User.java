package com.example.whattowatch.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_MOVIE_LISTS = "movieLists";
    public static final String KEY_PROFILE_PHOTO = "profilePhoto";

    public JSONArray getMovieLists(){
        return getJSONArray(KEY_MOVIE_LISTS);
    }

    public void setMovieLists(JSONArray movieLists){
        put(KEY_MOVIE_LISTS, movieLists);
    }

    public ParseFile getProfilePhoto(){
        return getParseFile(KEY_PROFILE_PHOTO);
    }

    public void setProfilePhoto(ParseFile profilePhoto){
        put(KEY_PROFILE_PHOTO, profilePhoto);
    }
}
