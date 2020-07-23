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
    }


    private void handleUncaught(Thread thread, Throwable e){
        Data.addLogData(getApplicationContext(), "handleUncaught: " + e.getMessage());
    }
}
