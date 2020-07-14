package com.szchoiceway.aios.bridge;

import android.app.Application;
import android.content.Context;

public class BridgeApplication extends Application {

    //private static final String DEFAULT_BACKGROUND_APP = "com.google.android.apps.maps";
    private static final String DEFAULT_BACKGROUND_APP = "net.dinglisch.android.taskerm";

    //private static final String DEFAULT_TOP_APP = "com.android.chrome";
    private static final String DEFAULT_TOP_APP = "com.waze";
    //private static final String DEFAULT_BOTTOM_APP = "com.google.android.apps.photos";
    //private static final String DEFAULT_BOTTOM_APP = "com.sirius";
    private static final String DEFAULT_BOTTOM_APP = "com.simplemobiletools.musicplayer";

    @Override
    public void onCreate() {
        super.onCreate();
        Context ctx = getApplicationContext();
        Data.addLogData(ctx, "BridgeApplication.onCreate()");

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException (Thread thread, Throwable e) {
                        handleUncaught (thread, e);
                    }
                });


        setDefaultIfEmpty(ctx, Data.BACKGROUND_APPS, DEFAULT_BACKGROUND_APP);
        setDefaultIfEmpty(ctx, Data.TOP_APP, DEFAULT_TOP_APP);
        setDefaultIfEmpty(ctx, Data.BOTTOM_APP, DEFAULT_BOTTOM_APP);
    }

    private void setDefaultIfEmpty(Context ctx, String name, String value){
        //TODO after implementing app selection, bring this back.
//        String current = Data.getPreference(ctx, name);
//        if (null == current || current.isEmpty()){
//            Data.setPreference(ctx, name, value);
//        }
        Data.setPreference(ctx, name, value);
    }

    private void handleUncaught(Thread thread, Throwable e){
        Data.addLogData(getApplicationContext(), "handleUncaught: " + e.getMessage());
    }
}
