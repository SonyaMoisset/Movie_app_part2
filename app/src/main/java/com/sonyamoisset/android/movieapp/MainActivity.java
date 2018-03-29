package com.sonyamoisset.android.movieapp;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.sonyamoisset.android.movieapp.adapter.MoviesAdapter;
import com.sonyamoisset.android.movieapp.api.MoviesApiClient;
import com.sonyamoisset.android.movieapp.api.MoviesApiInterface;
import com.sonyamoisset.android.movieapp.api.MoviesApiParams;
import com.sonyamoisset.android.movieapp.data.MovieContract;
import com.sonyamoisset.android.movieapp.fragment.FavoriteMoviesFragment;
import com.sonyamoisset.android.movieapp.fragment.PopularMoviesFragment;
import com.sonyamoisset.android.movieapp.fragment.TopRatedMoviesFragment;
import com.sonyamoisset.android.movieapp.model.Movie;
import com.sonyamoisset.android.movieapp.model.MoviesResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sonyamoisset.android.movieapp.utils.NetworkConnectivity.isConnected;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected(this)) {
            initMoviesGridView();
            createBottomNavigationView();
        } else {
            Toast.makeText(this, R.string.message_no_connectivity, Toast.LENGTH_LONG).show();
        }
    }

    private void createBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {

                            case R.id.sort_by__favorites_movies:
                                selectedFragment = FavoriteMoviesFragment.newInstance();
                                getMovies(getString(R.string.main_activity_sortBy_favorites_movies));
                                break;

                            case R.id.sort_by_popular_movies:
                                selectedFragment = PopularMoviesFragment.newInstance();
                                getMovies(getString(R.string.main_activity_sortBy_popular_movies));
                                break;

                            case R.id.sort_by_top_rated_movies:
                                selectedFragment = TopRatedMoviesFragment.newInstance();
                                getMovies(getString(R.string.main_activity_sortBy_top_rated_movies));
                                break;
                        }

                        FragmentTransaction transaction =
                                getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();

                        return true;
                    }
                });
    }

    private void initMoviesGridView() {

        recyclerView = findViewById(R.id.recyclerView);

        Context context = this;
        MoviesAdapter moviesAdapter = new MoviesAdapter(this, movieList);

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.notifyDataSetChanged();

        getMovies(getString(R.string.main_activity_sortBy_popular_movies));
    }

    private void showFavorites() {

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (!cursor.moveToNext()) {
                Toast.makeText(this, getResources().getText(R.string.no_movies_liked), Toast.LENGTH_SHORT).show();

            } else {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Movie movie = new Movie(
                            cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE)),
                            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)),
                            cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE))
                    );

                    if (movieList != null) {
                        movieList.add(movie);
                    }
                    recyclerView.setAdapter(
                            new MoviesAdapter(getApplicationContext(), movieList));
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void getMovies(String sortBy) {
        try {
            MoviesApiInterface moviesApiInterface =
                    MoviesApiClient.getClient().create(MoviesApiInterface.class);

            Call<MoviesResult> moviesResponse = null;

            if (Objects.equals(sortBy, getString(R.string.main_activity_sortBy_popular_movies))) {
                moviesResponse =
                        moviesApiInterface.getPopularMovies(MoviesApiParams.API_KEY);
            }
            if (Objects.equals(sortBy, getString(R.string.main_activity_sortBy_top_rated_movies))) {
                moviesResponse =
                        moviesApiInterface.getTopRatedMovies(MoviesApiParams.API_KEY);
            }
            if (Objects.equals(sortBy, getString(R.string.main_activity_sortBy_favorites_movies))) {
                showFavorites();
            }

            if (moviesResponse != null) {
                moviesResponse.enqueue(new Callback<MoviesResult>() {

                    @Override
                    public void onResponse(@NonNull Call<MoviesResult> call,
                                           @NonNull Response<MoviesResult> response) {
                        movieList = response.body().getListOfMovies();
                        recyclerView.setAdapter(
                                new MoviesAdapter(getApplicationContext(), movieList));
                        recyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResult> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
