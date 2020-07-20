package com.example.whattowatch.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whattowatch.FindMovieActivity;
import com.example.whattowatch.NewMovieListActivity;
import com.example.whattowatch.R;
import com.example.whattowatch.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FindMoviesAdapter extends RecyclerView.Adapter<FindMoviesAdapter.ViewHolder>{
    public static final String TAG = "FindMoviesAdapter";

    private Context context;
    private List<Movie> movies;

    public FindMoviesAdapter(Context context, List<Movie> movies) {
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

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getDescription());
            Glide.with(context).load(movie.getPosterPath()).into(ivPoster);
        }


        @Override
        public void onClick(View view) {
            // get the item position
            int position = getAdapterPosition();
            Log.d(TAG, "Clicked on position " + position);
            // makes sure position is valid
            if (position != RecyclerView.NO_POSITION){
                // TODO send the movie in position back to newMovieListActivity
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Movie.class.getSimpleName(), movies.get(position));
                if (context instanceof Activity) {
                    ((Activity) context).setResult(RESULT_OK, returnIntent);
                    ((Activity) context).finish();
                }
                else {
                    Log.e(TAG, "context wasn't instance of Activity");
                }

            }
        }
    }
}