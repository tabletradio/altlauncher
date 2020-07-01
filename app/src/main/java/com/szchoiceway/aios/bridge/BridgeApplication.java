package com.szchoiceway.aios.bridge;

import android.app.Application;

public class BridgeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Data.addData(getApplicationContext(), "BridgeApplication.onCreate()");

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException (Thread thread, Throwable e) {
                        handleUncaught (thread, e);
                    }
                });
    }

    private void handleUncaught(Thread thread, Throwable e){
        Data.addData(getApplicationContext(), "handleUncaught: " + e.getMessage());
    }
}
