package com.sharonomokwale.cryptopal.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MarketUtil {
    public static String ID = "ID";
    public static String NAME = "NAME";
    public static String LOGO = "LOGO";
    public static String URL = "url";
    public static String APIKEY = "2d6f1f92684971b2967387d222f203a6d2e74d296c0caa6242c2661223783d4e";
    public static String DETAIL = "https://min-api.cryptocompare.com/data/top/totalvolfull?limit=30&tsym=USD";


    public static boolean isConnected(Context ctx) {
        ConnectivityManager mgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
