package com.sonyamoisset.android.movieapp.api;

import com.sonyamoisset.android.movieapp.BuildConfig;

public class MoviesApiParams {
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    static final String POPULAR_PATH = "movie/popular?api_key=";
    static final String TOP_RATED_PATH = "movie/top_rated?api_key=";
    static final String TRAILERS = "movie/{movie_id}/videos";
    static final String REVIEWS = "movie/{movie_id}/reviews";
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
}
