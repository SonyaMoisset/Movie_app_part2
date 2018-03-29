package com.sonyamoisset.android.movieapp.api;

import com.sonyamoisset.android.movieapp.model.MoviesResult;
import com.sonyamoisset.android.movieapp.model.ReviewsResult;
import com.sonyamoisset.android.movieapp.model.TrailersResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiInterface {

    @GET(MoviesApiParams.POPULAR_PATH)
    Call<MoviesResult> getPopularMovies(@Query("api_key") String apiKey);

    @GET(MoviesApiParams.TOP_RATED_PATH)
    Call<MoviesResult> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET(MoviesApiParams.TRAILERS)
    Call<TrailersResult> getListOfTrailers(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET(MoviesApiParams.REVIEWS)
    Call<ReviewsResult> getListOfReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
