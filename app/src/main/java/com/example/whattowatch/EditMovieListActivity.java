package com.example.whattowatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.whattowatch.adapters.RecommenderAdapter;
import com.example.whattowatch.models.Movie;
import com.example.whattowatch.models.MovieList;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class EditMovieListActivity extends AppCompatActivity {
    public static final String TAG = "EditMovieListActivity";
    public static final int REQUEST_CODE = 5;

    MovieList movieList;

    Button btnEditFindMovie;
    Button btnFinishEditList;
    RecyclerView rvEditMovieList;
    EditText etEditMovieListName;
    List<Movie> movies;
    RecommenderAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie_list);

        btnEditFindMovie = findViewById(R.id.btnEditFindMovie);
        rvEditMovieList = findViewById(R.id.rvEditMovieList);
        btnFinishEditList = findViewById(R.id.btnFinishEditList);
        etEditMovieListName = findViewById(R.id.etEditMovieListName);

        btnEditFindMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMovie = new Intent(EditMovieListActivity.this, FindMovieActivity.class);
                startActivityForResult(newMovie, REQUEST_CODE);
            }
        });

        // unwrap the movieList passed in via intent, using its simple name as a key
        movieList = (MovieList) Parcels.unwrap(getIntent().getParcelableExtra(MovieList.class.getSimpleName()));
        etEditMovieListName.setText(movieList.getTitle());
        assert movieList != null;
        movies = movieList.getListOfMovies();

        movieAdapter = new RecommenderAdapter(this, movies);
        // set the adapter as the recycler view adapter
        rvEditMovieList.setAdapter(movieAdapter);
        // Set a layout Manager on the recycler view
        rvEditMovieList.setLayoutManager(new LinearLayoutManager(this));

        btnFinishEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbNewMovieList);
        setSupportActionBar(toolbar);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
                final int adapterPosition = viewHolder.getAdapterPosition();

                // delete movie list
                movies.remove(adapterPosition);
                movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(EditMovieListActivity.this, R.color.swipeLeftBackground))
                        .addSwipeLeftActionIcon(R.drawable.trash)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvEditMovieList);
    }

    /**
     *  We are overwriting onActivityResult so when we get back, we add the selected movie in the previous activity to our recyclerView
     *  and then notify the recyclerView adapter that the recyclerView changed and must be updated
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            Movie movie = (Movie) data.getParcelableExtra(Movie.class.getSimpleName());
            movies.add(movie);
            movieAdapter.notifyDataSetChanged();
        }
    }
}