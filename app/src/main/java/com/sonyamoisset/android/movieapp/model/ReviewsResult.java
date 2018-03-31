package com.sonyamoisset.android.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResult {
    @SerializedName("results")
    private final List<Review> results;

    public ReviewsResult(List<Review> results) {
        this.results = results;
    }

    public List<Review> getListOfReviews() {
        return results;
    }
}
