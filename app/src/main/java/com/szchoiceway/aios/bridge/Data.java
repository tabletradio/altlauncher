package com.szchoiceway.aios.bridge;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.preference.PreferenceManager;

public class Data {

    private static final String LOG_PREF_NAME = "LOG_DATA";

    public static final String BACKGROUND_APPS = "BACKGROUND_APPS";
    public static final String SERVICE_APPS = "SERVICE_APPS";
    public static final String TOP_APP = "TOP_APP";
    public static final String BOTTOM_APP = "BOTTOM_APP";

    public static void addLogData(Context context, String data){
        String prev = getLogData(context);
        if (prev.length() > 10000){
            prev = "";
        }
        String upd = prev + "\n" + getDateTime() + ": " + data;
        setPreference(context, LOG_PREF_NAME, upd);
    }

    public static void setPreference(Context context, String name, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(name, value);
        edit.commit();
    }

    public static void clearLogData(Context context){
        clearData(context, LOG_PREF_NAME);
        addLogData(context, "Cleared data");
    }

    public static void clearData(Context context, String name){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(name, "");
        edit.commit();
    }

    public static String getLogData(Context context){
        return getPreference(context, LOG_PREF_NAME);
    }

    public static String getPreference(Context context, String name){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(name, "");
    }

    public static long getLongPreference(Context context, String name){
        String val = getPreference(context, name);
        long answer = 0;
        try {
            answer = Long.parseLong(val);
        } catch(NumberFormatException e){
            ;
        }
        return answer;
    }

    private static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
