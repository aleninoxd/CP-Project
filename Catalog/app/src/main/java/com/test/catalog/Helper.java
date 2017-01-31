package com.test.catalog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class Helper {

    public static final String uri = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    public static String getJsonString(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getString("data", "");
    }

    public static void setJsonString(Activity context, String string) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data", string);
        editor.apply();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
