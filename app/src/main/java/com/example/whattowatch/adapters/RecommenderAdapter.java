package com.example.whattowatch.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.whattowatch.MovieDetailActivity;
import com.example.whattowatch.MovieListDetail;
import com.example.whattowatch.R;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecommenderAdapter extends RecyclerView.Adapter<RecommenderAdapter.ViewHolder>{
    public static final String TAG = "RecommenderAdapter";

    private Context context;
    private List<Movie> movies;

    public RecommenderAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private TextView tvOverview;
        private ImageView ivPoster;
        private CardView cvMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            cvMovie = itemView.findViewById(R.id.cvMovie);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getDescription());

            Glide.with(context)
                    .asBitmap()
                    .load(movie.getPosterPath())
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
                                        cvMovie.setBackgroundColor(vibrant.getRgb());
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

        }

        @Override
        public void onClick(View view) {
            // get the item position
            int position = getAdapterPosition();
            Log.d(TAG, "Clicked on position " + position);
            // makes sure position is valid
            if (position != RecyclerView.NO_POSITION){
                Intent movieDetailIntent = new Intent(context, MovieDetailActivity.class);
                movieDetailIntent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movies.get(position)));
                Log.d(TAG, "MovieList being passed: " + movies.get(position));
                context.startActivity(movieDetailIntent);

            }
        }
    }
}
