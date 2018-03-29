package com.sonyamoisset.android.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResult {

    @SerializedName("results")
    private final List<Trailer> results;

    public TrailersResult(List<Trailer> results) {
        this.results = results;
    }

    public List<Trailer> getListOfTrailers() {
        return results;
    }
}
