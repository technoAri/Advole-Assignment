package com.example.hppc.mychatdemo;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by HP PC on 2/19/2017.
 */
public class InternetConnection {
    public static boolean checkConnection(Context context) {
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
