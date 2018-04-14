package com.sonyamoisset.android.movieapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sonyamoisset.android.movieapp.R;

public class FavoriteMoviesFragment extends Fragment {
    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_movies, container, false);
    }
}
