package com.example.whattowatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.whattowatch.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    //RatingBar rbVoteAverage;
    ImageView ivPoster;
    CardView cvMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        //rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        cvMovieDetail = findViewById(R.id.cvMovieDetail);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getDescription());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        //float voteAverage = movie.getVoteAverage().floatValue();
        //rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // get image in movie details view
        String imageUrl;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // if phone is in landscape
            imageUrl = movie.getBackdropPath();
        } else {
            // if phone is in portrait
            imageUrl = movie.getPosterPath();
        }
        int radius = 3;
        int margin = 1;
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivPoster.setImageBitmap(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Get the "vibrant" color swatch based on the bitmap
                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                if (vibrant != null) {
                                    // Set the background color of a layout based on the vibrant color
                                    cvMovieDetail.setBackgroundColor(vibrant.getRgb());
                                    // Update the title TextView with the proper text color
                                    tvTitle.setTextColor(vibrant.getTitleTextColor());
                                    tvOverview.setTextColor(vibrant.getTitleTextColor());

                                }
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://api.themoviedb.org/3/movie/" + movie.getID() + "/videos?api_key=557bf444fa647aa33e0e1a2de0317f55", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    final String youtubePath = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");
                    ivPoster.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MovieDetailActivity.this, MovieTrailerActivity.class);
                            i.putExtra("youtubePath", youtubePath);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("MovieDetails", "waiting");
            }
        });



    }

}