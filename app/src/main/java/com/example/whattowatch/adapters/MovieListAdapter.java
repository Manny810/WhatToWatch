package com.example.whattowatch.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whattowatch.R;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapters are the brains behind the recyclerView, as they are responsible for setting up each row and updating the recyclerView
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{
    public static final String TAG = "NewMovieListAdapter";

    private Context context;
    private List<MovieList> movieLists;

    public MovieListAdapter(Context context, List<MovieList> movieLists) {
        this.context = context;
        this.movieLists = movieLists;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_list, parent, false);

        return new MovieListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
        MovieList movieList = movieLists.get(position);
        holder.bind(movieList);
    }

    @Override
    public int getItemCount() {
        return movieLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieListTitle;
        TextView tvMovieListSize;
        TextView tvMovieListCreatedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieListTitle = itemView.findViewById(R.id.tvMovieListTitle);
            tvMovieListSize = itemView.findViewById(R.id.tvMovieListSize);
            tvMovieListCreatedAt = itemView.findViewById(R.id.tvMovieListCreatedAt);
        }

        public void bind(MovieList movieList) {
            tvMovieListTitle.setText(movieList.getTitle());
            tvMovieListSize.setText(String.format("Number of movies: %d", movieList.getMovies().length()));
            tvMovieListCreatedAt.setText(String.format("Created %s", getRelativeTimeAgo(movieList.getCreatedAt().toString())));

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
}
