package com.example.whattowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.whattowatch.adapters.RecommenderAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;

import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MovieListDetail extends AppCompatActivity {
    public static final String TAG = "MovieListDetail";

    TextView tvListTitle;
    TextView tvListSize;
    TextView tvListCreatedAt;
    TextView tvRecommendationSize;

    RecyclerView rvMovieListDetail;

    MovieList movieList;
    List<Movie> movies;

    RecommenderAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_detail);

        tvListTitle = findViewById(R.id.tvListTitle);
        tvListSize = findViewById(R.id.tvListSize);
        tvListCreatedAt = findViewById(R.id.tvListCreatedAt);
        tvRecommendationSize = findViewById(R.id.tvRecommendationSize);
        rvMovieListDetail = findViewById(R.id.rvMovieListDetail);

        // unwrap the movieList passed in via intent, using its simple name as a key
        movieList = (MovieList) Parcels.unwrap(getIntent().getParcelableExtra(MovieList.class.getSimpleName()));

        assert movieList != null;
        movies = movieList.getListOfMovies();
        movieAdapter = new RecommenderAdapter(this, movies);
        rvMovieListDetail.setAdapter(movieAdapter);
        rvMovieListDetail.setLayoutManager(new LinearLayoutManager(this));

        tvListTitle.setText(movieList.getTitle());
        tvListSize.setText(String.format("Number of Movies: %d", movies.size()));
        tvListCreatedAt.setText(String.format("Created %s", getRelativeTimeAgo(movieList.getCreatedAt().toString())));

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}