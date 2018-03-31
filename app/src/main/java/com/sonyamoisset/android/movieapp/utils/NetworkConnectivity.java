package com.sonyamoisset.android.movieapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sonyamoisset.android.movieapp.ui.activity.MainActivity;

public class NetworkConnectivity {
    public static boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
