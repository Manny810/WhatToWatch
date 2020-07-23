package com.example.whattowatch.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whattowatch.CameraActivity;
import com.example.whattowatch.NewMovieListActivity;
import com.example.whattowatch.R;
import com.example.whattowatch.adapters.MovieListAdapter;
import com.example.whattowatch.adapters.NewMovieListAdapter;
import com.example.whattowatch.models.MovieList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;


public class MovieListFragment extends Fragment {
    public static final String TAG = "MovieListFragment";

    RecyclerView rvMovieLists;
    Button btnNewList;

    MovieListAdapter movieAdapter;
    List<MovieList> movieLists;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();

        btnNewList = view.findViewById(R.id.btnNewList);
        rvMovieLists = view.findViewById(R.id.rvMovieLists);

        btnNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieListIntent = new Intent(getContext(), NewMovieListActivity.class);
                startActivityForResult(movieListIntent, 2);
            }
        });

        movieLists = new ArrayList<>();

        // create adapter for RecyclerView
        movieAdapter = new MovieListAdapter(getContext(), movieLists);

        // set the adapter as the recycler view adapter
        rvMovieLists.setAdapter(movieAdapter);

        // Set a layout Manager on the recycler view
        rvMovieLists.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                        .addActionIcon(R.drawable.trash)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvMovieLists);

        queryLists();

    }

    protected void queryLists() {
        // Specify which class to query
        ParseQuery<MovieList> query = ParseQuery.getQuery(MovieList.class);
        query.include(MovieList.KEY_USER);
        query.whereEqualTo(MovieList.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(MovieList.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<MovieList>() {
            @Override
            public void done(List<MovieList> newMovieLists, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (MovieList movieList: newMovieLists){
                    Log.i(TAG, "MovieList: " + movieList.getTitle() + ", username: " + movieList.getUser().getUsername());
                }
                movieLists.clear();
                movieLists.addAll(newMovieLists);
                movieAdapter.notifyDataSetChanged();
            }
        });

    }

    // Calling queryLists when we get back to movieListFragment so that we can repull from the database and get the latest movie lists
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called");
        if (requestCode == 2 && resultCode == RESULT_OK){
            Log.d(TAG, "newMovie being added");
            assert data != null;
            MovieList newMovieList = (MovieList) Parcels.unwrap(data.getParcelableExtra(MovieList.class.getSimpleName()));
            movieLists.add(0, newMovieList);
            movieAdapter.notifyDataSetChanged();
        }
    }


}