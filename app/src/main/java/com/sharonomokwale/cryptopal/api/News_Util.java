package com.sharonomokwale.cryptopal.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class News_Util {

    public static String ID = "ID";
    public static String NAME = "NAME";
    public static String LOGO = "LOGO";
    public static String URL = "url";
    //public static String APIKEY = "add701b831aa490094f63c0d4e521741";
    public static String APIKEY="2d6f1f92684971b2967387d222f203a6d2e74d296c0caa6242c2661223783d4e";
   // public static String DETAIL = "https://newsapi.org/v2/everything?q=bitcoin";
   public static String DETAIL= "https://min-api.cryptocompare.com/data/v2/news/?lang=EN";
   // https://cryptopanic.com/api/v1/posts/?auth_token=a480969058dd25a58dc551f34080d6c816b34137&public=true
    public static String BASEURL = "https://newsapi.org/v2/sources?";
   // https://newsapi.org/v2/everything?q=bitcoin


    public static boolean isConnected(Context ctx) {
        ConnectivityManager mgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();


    }
}
