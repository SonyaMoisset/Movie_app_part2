package com.sonyamoisset.android.movieapp.ui.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sonyamoisset.android.movieapp.BuildConfig;
import com.sonyamoisset.android.movieapp.R;
import com.sonyamoisset.android.movieapp.adapter.ReviewsAdapter;
import com.sonyamoisset.android.movieapp.adapter.TrailersAdapter;
import com.sonyamoisset.android.movieapp.api.MoviesApiClient;
import com.sonyamoisset.android.movieapp.api.MoviesApiInterface;
import com.sonyamoisset.android.movieapp.data.MovieContract;
import com.sonyamoisset.android.movieapp.model.Movie;
import com.sonyamoisset.android.movieapp.model.Review;
import com.sonyamoisset.android.movieapp.model.ReviewsResult;
import com.sonyamoisset.android.movieapp.model.Trailer;
import com.sonyamoisset.android.movieapp.model.TrailersResult;
import com.sonyamoisset.android.movieapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.movie_backdrop_path)
    ImageView movieBackdropPath;
    @BindView(R.id.movie_original_title)
    TextView movieName;
    @BindView(R.id.movie_overview)
    TextView moviePlot;
    @BindView(R.id.movie_vote_average)
    TextView movieVoteAverage;
    @BindView(R.id.movie_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.favorite)
    Button favorite;

    public RecyclerView trailerRecyclerView;
    public RecyclerView reviewRecyclerView;
    Movie movie;
    Cursor cursor;
    private int movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        populateMovieDetailUI();
    }

    private void populateMovieDetailUI() {
        movie = getIntent().getParcelableExtra(getString(R.string.MOVIE_KEY));
        movie_id = movie.getId();

        String poster = Constants.MOVIE_POSTER_PATH;
        String backdrop = Constants.MOVIE_BACKDROP_IMAGE_PATH;

        Picasso.with(this)
                .load(poster + movie.getPosterPath())
                .placeholder(R.color.colorSecondary)
                .into(moviePoster);

        Picasso.with(this)
                .load(backdrop + movie.getBackdropPath())
                .placeholder(R.color.colorSecondary)
                .into(movieBackdropPath);

        movieName.setText(movie.getOriginalTitle());
        moviePlot.setText(movie.getOverview());
        movieVoteAverage.setText(String.format("%s", movie.getVoteAverage()));
        movieReleaseDate.setText(movie.getReleaseDate());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(movie.getOriginalTitle());
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFavorites()) {
                    deleteFromFavorites();
                } else {
                    addToFavorites();
                }
                cursor.close();
            }
        });

        initMovieDetailTrailersView();
        initMovieDetailReviewsView();
    }

    private void addToFavorites() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_id);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());

        resolver.insert(uri, values);

        Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void deleteFromFavorites() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movie_id)};

        resolver.delete(uri, selection, selectionArgs);

        Toast.makeText(getApplicationContext(), "Movie removed from favorites", Toast.LENGTH_SHORT).show();
    }

    private boolean checkFavorites() {
        cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie_id)},
                null);

        return (cursor != null) && (cursor.getCount() > 0);
    }

    private void initMovieDetailTrailersView() {
        List<Trailer> trailerList = new ArrayList<>();
        TrailersAdapter trailersAdapter = new TrailersAdapter(this, trailerList);
        trailerRecyclerView = findViewById(R.id.trailerRecyclerView);

        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setAdapter(trailersAdapter);
        trailersAdapter.notifyDataSetChanged();

        getMovieDetailTrailers();
    }

    private void initMovieDetailReviewsView() {
        List<Review> reviewList = new ArrayList<>();
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviewList);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();

        getMovieDetailReviews();
    }

    private void getMovieDetailTrailers() {
        try {
            MoviesApiInterface moviesApiInterface =
                    MoviesApiClient.getClient().create(MoviesApiInterface.class);

            Call<TrailersResult> trailersResponse =
                    moviesApiInterface.getListOfTrailers(movie_id, BuildConfig.THE_MOVIE_DB_API_KEY);

            if (trailersResponse != null) {
                trailersResponse.enqueue(new Callback<TrailersResult>() {

                    @Override
                    public void onResponse(@NonNull Call<TrailersResult> call,
                                           @NonNull Response<TrailersResult> response) {
                        List<Trailer> trailers = response.body().getListOfTrailers();
                        trailerRecyclerView.setAdapter(
                                new TrailersAdapter(getApplicationContext(), trailers));
                        trailerRecyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailersResult> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMovieDetailReviews() {
        try {
            MoviesApiInterface moviesApiInterface =
                    MoviesApiClient.getClient().create(MoviesApiInterface.class);

            Call<ReviewsResult> reviewsResponse =
                    moviesApiInterface.getListOfReviews(movie_id, BuildConfig.THE_MOVIE_DB_API_KEY);

            if (reviewsResponse != null) {
                reviewsResponse.enqueue(new Callback<ReviewsResult>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewsResult> call,
                                           @NonNull Response<ReviewsResult> response) {
                        List<Review> reviews = response.body().getListOfReviews();
                        reviewRecyclerView.setAdapter(
                                new ReviewsAdapter(reviews));
                        reviewRecyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewsResult> call,
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