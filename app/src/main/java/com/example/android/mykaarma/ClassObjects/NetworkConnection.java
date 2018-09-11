package com.example.android.mykaarma.ClassObjects;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class NetworkConnection
{
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
