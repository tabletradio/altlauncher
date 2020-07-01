package com.szchoiceway.aios.bridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szchoiceway.aios.bridge.DaemonService;
import com.szchoiceway.aios.bridge.Data;

public class BridgeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Data.addData(context, "BridgeReceiver.onReceive(). Action = " + action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)){
            Data.addData(context, "BridgeReceiver.onReceive(). Asking service to start app.");
            Intent in = new Intent(context, DaemonService.class);
            in.putExtra(DaemonService.LAUNCH_APPS, true);
            in.putExtra(DaemonService.SCREEN_TURN_ON, false);
            context.startForegroundService(in);
        }
    }
}
