package com.example.whattowatch;

import android.app.Application;

import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;
import com.example.whattowatch.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(MovieList.class);
        ParseObject.registerSubclass(Movie.class);
        ParseObject.registerSubclass(User.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("manny-what-to-watch") // should correspond to APP_ID env variable
                .clientKey("CodepathMoveFastParse")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://manny-what-to-watch.herokuapp.com/parse").build());
    }
}

