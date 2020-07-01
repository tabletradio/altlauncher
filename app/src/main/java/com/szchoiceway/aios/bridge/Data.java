package com.szchoiceway.aios.bridge;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.preference.PreferenceManager;

public class Data {

    private static final String PREF_NAME = "LOG_DATA";

    public static void addData(Context context, String data){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prev = prefs.getString(PREF_NAME, "");
        if (prev.length() > 10000){
            prev = "";
        }
        String upd = prev + "\n" + getDateTime() + ": " + data;
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREF_NAME, upd);
        edit.commit();
    }

    public static void clearData(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREF_NAME, "");
        edit.commit();
        addData(context, "Cleared data");
    }

    public static String getData(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prev = prefs.getString(PREF_NAME, "");
        return prev;
    }

    private static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
