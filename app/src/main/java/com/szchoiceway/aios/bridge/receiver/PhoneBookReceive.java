package com.szchoiceway.aios.bridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szchoiceway.aios.bridge.DaemonService;
import com.szchoiceway.aios.bridge.Data;

public class PhoneBookReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Data.addLogData(context, "PhoneBookReceive.onReceive(). Action = " + action);
        if (action.equals(Intent.ACTION_SCREEN_ON)){
            Data.addLogData(context, "PhoneBookReceive.onReceive. Asking for service to start app.");
            Intent in = new Intent(context, DaemonService.class);
            in.putExtra(DaemonService.LAUNCH_APPS, true);
            in.putExtra(DaemonService.SCREEN_TURN_ON, true);
            context.startForegroundService(in);
        }
    }
}
