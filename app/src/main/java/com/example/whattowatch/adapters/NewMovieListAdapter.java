package com.example.whattowatch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.example.whattowatch.R;
import com.example.whattowatch.models.Movie;

import java.util.List;

public class NewMovieListAdapter extends RecyclerView.Adapter<NewMovieListAdapter.ViewHolder>{
    public static final String TAG = "NewMovieListAdapter";

    private Context context;
    private List<Movie> movies;

    public NewMovieListAdapter(Context context, List<Movie> movies) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

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
    }
}
