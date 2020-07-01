package com.szchoiceway.aios.bridge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.szchoiceway.aios.bridge.receiver.PhoneBookReceive;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;

//https://developer.android.com/guide/components/services
//https://github.com/madebyfrancisco/SplitScreenLauncher
public class DaemonService extends Service {
    private static final String CHANNEL_ID = "mynotificationchannel";
    //private static final String BACKGROUND_APP = "com.google.android.apps.maps";
    private static final String BACKGROUND_APP = "net.dinglisch.android.taskerm";

    //private static final String TOP_APP = "com.android.chrome";
    private static final String TOP_APP = "com.waze";
    //private static final String BOTTOM_APP = "com.google.android.apps.photos";
    private static final String BOTTOM_APP = "com.sirius";

    public static final String LAUNCH_APPS = "launch_apps";
    public static final String SCREEN_TURN_ON = "screen_turn_on";

    public static final String WINDOW_MODE = "android.activity.windowingMode";
    public static final int SPLIT_PRIMARY = 3;
    public static final int SPLIT_SECONDARY = 4;
    public static final String CREATE_MODE = "android:activity.splitScreenCreateMode";
    public static final int SPLIT_TOP = 0;
    public static final int SPLIT_BOTTOM=1;


    private class Logger implements Runnable {
        private Context ctx;

        public Logger(Context context){
            this.ctx = context;
        }

        @Override
        public void run() {
            while (true) {
                Data.addData(this.ctx, "background thread.run.");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Logger runner = null;
    PhoneBookReceive receive = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Data.addData(getApplicationContext(), "DaemonService.onCreate().");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Data.addData(getApplicationContext(), "DaemonService.onStartCommand().");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pending = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service Started")
                .setTicker("This is a ticker")
                .setContentText("Click to start App")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentIntent(pending)
                .build();

        startForeground(1001, notification);

        if (null == runner) {
            runner = new Logger(this.getApplicationContext());
            new Thread(runner).start();
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        if (receive == null) {
            receive = new PhoneBookReceive();
            getApplicationContext().registerReceiver(receive, intentFilter);
        }

        if (null != intent) {
            boolean launch = intent.getBooleanExtra(LAUNCH_APPS, false);
            if (launch) {
                boolean screen_on = intent.getBooleanExtra(SCREEN_TURN_ON, false);
                startApps(getApplicationContext(), screen_on);
            }
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    public DaemonService() {
        //createNotificationChannel();
        //Data.addData(this, "DaemonService constructor.");
    }

    private void startApps(Context context, boolean screen_turn_on){
        if (screen_turn_on){
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Data.addData(context, "DaemonService --- Interrupted!");
            }
        }

        //start app
        Data.addData(context, "DaemonService -- Starting Apps!");

        Intent background = context.getPackageManager().getLaunchIntentForPackage(BACKGROUND_APP);
        if (null != background) {
            background.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            background.addCategory("android.intent.category.LAUNCHER");
            Data.addData(context, "DaemonService -- Launching Background App!");
            context.startActivity(background);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Intent top_intent = context.getPackageManager().getLaunchIntentForPackage(TOP_APP);
        if (null != top_intent) {
            top_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            top_intent.addCategory("android.intent.category.LAUNCHER");
            Bundle bundle = ActivityOptionsCompat.makeBasic().toBundle();
            if (bundle != null) {
                bundle.putInt(WINDOW_MODE, SPLIT_PRIMARY);
                bundle.putInt(CREATE_MODE, SPLIT_TOP);
            }
            Data.addData(context, "DaemonService -- Launching Top App!");
            context.startActivity(top_intent, bundle);
        }

        Intent bottom_intent = context.getPackageManager().getLaunchIntentForPackage(BOTTOM_APP);
        if (null != bottom_intent) {
            bottom_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            bottom_intent.addCategory("android.intent.category.LAUNCHER");
            Bundle bundle = ActivityOptionsCompat.makeBasic().toBundle();
            if (bundle != null) {
                bundle.putInt(WINDOW_MODE, SPLIT_SECONDARY);
                bundle.putInt(CREATE_MODE, SPLIT_BOTTOM);
            }
            Data.addData(context, "DaemonService -- Launching Bottom App!");
            context.startActivity(bottom_intent, bundle);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        Data.addData(getApplicationContext(), "DaemonService destructor.");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        if (null != receive) {
            unregisterReceiver(receive);
            receive = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Data.addData(getApplicationContext(), "DaemonService.onBind().");
        // We don't provide binding, so return null
        return null;
    }
}
