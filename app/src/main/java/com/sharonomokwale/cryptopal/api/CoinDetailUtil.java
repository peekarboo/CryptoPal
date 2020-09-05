package com.sharonomokwale.cryptopal.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CoinDetailUtil {

    //public static String DETAIL = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=3";
    public static String APIKEY = "2d6f1f92684971b2967387d222f203a6d2e74d296c0caa6242c2661223783d4e";

    public static boolean isConnected(Context ctx) {
        ConnectivityManager mgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
