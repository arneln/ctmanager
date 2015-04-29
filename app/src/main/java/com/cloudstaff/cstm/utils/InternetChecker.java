package com.cloudstaff.cstm.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetChecker {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

}
