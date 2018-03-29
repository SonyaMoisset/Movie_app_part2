package com.sonyamoisset.android.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    private final Integer id;
    @SerializedName("poster_path")
    private final String posterPath;
    @SerializedName("overview")
    private final String overview;
    @SerializedName("release_date")
    private final String releaseDate;
    @SerializedName("original_title")
    private final String originalTitle;
    @SerializedName("backdrop_path")
    private final String backdropPath;
    @SerializedName("vote_average")
    private final Double voteAverage;

    public Movie(Integer id, String posterPath, String overview, String releaseDate,
                 String originalTitle, String backdropPath, Double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    public Integer getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(posterPath);
        out.writeString(overview);
        out.writeString(releaseDate);
        out.writeString(originalTitle);
        out.writeString(backdropPath);
        out.writeDouble(voteAverage);
    }
}
