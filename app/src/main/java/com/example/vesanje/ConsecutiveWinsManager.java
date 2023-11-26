package com.example.vesanje;

import android.content.Context;
import android.content.SharedPreferences;

public class ConsecutiveWinsManager {
    private static final String PREF_NAME = "consecutive_wins";
    private static final String PREF_NAME_RAND = "consecutive_wins_random";
    private static final String random = "random";

    public static void setConsecutiveWins(Context context, String categoryName, int consecutiveWins) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(categoryName, consecutiveWins);
        editor.apply();
    }

    public static int getConsecutiveWins(Context context, String categoryName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(categoryName, 0);
    }

    public static void setConsecutiveWinsRandom(Context context, int consecutiveWins) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME_RAND, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(random, consecutiveWins);
        editor.apply();
    }

    public static int getConsecutiveWinsRandom(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME_RAND, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(random, 0);
    }
}