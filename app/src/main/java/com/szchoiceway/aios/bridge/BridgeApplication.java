package com.szchoiceway.aios.bridge;

import android.app.Application;
import android.content.Context;

public class BridgeApplication extends Application {


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
        String currentVal = Data.getPreference(ctx, "delay_after_app_launch");
        if (null == currentVal || currentVal.trim().isEmpty()){
            Data.setPreference(ctx, "delay_after_app_launch", "4");
        }
        currentVal = Data.getPreference(ctx, "delay_after_boot");
        if (null == currentVal || currentVal.trim().isEmpty()){
            Data.setPreference(ctx, "delay_after_boot", "0");
        }
        currentVal = Data.getPreference(ctx, "delay_after_fast_boot");
        if (null == currentVal || currentVal.trim().isEmpty()){
            Data.setPreference(ctx, "delay_after_fast_boot", "15");
        }
    }


    private void handleUncaught(Thread thread, Throwable e){
        Data.addLogData(getApplicationContext(), "handleUncaught: " + e.getMessage());
    }
}
