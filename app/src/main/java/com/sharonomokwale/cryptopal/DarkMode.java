package com.sharonomokwale.cryptopal;
import android.content.Context;
import android.content.SharedPreferences;

public class DarkMode {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "education-dark-mode";
    private static final String IS_NIGHT_MODE = "IsNightMode";

    public DarkMode(Context context) {
        this._context = context;
        sp = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public void setDarkMode(boolean isFirstTime) {
        editor.putBoolean(IS_NIGHT_MODE, isFirstTime);
        editor.commit();
    }

    public boolean isNightMode() {
        return sp.getBoolean(IS_NIGHT_MODE, true);
    }

}

