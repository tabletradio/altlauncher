package com.szchoiceway.aios.bridge.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.List;

public class AppUtil {

    public static String getAppActivityName(PackageManager pm, String pkg){
        String nm = "";
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = pm.queryIntentActivities(main,0);
        if (null != infos && !infos.isEmpty()) {
            for (ResolveInfo in : infos){
                if (null != in && null != in.activityInfo && null != in.activityInfo.packageName){
                    if (in.activityInfo.packageName.equals(pkg)){
                        nm = in.activityInfo.name;
                    }
                }
            }
        }
        return nm;
    }

    public static String getAppName(String pkg, PackageManager pm) {
        String nm = pkg;
        try {
            CharSequence cs = pm.getApplicationLabel(pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA));
            nm = cs.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        };
        return nm;
    }

    public static Drawable getAppIcon(String pkg, PackageManager pm){
        Drawable dr;
        try {
            dr = pm.getApplicationIcon(pkg);
        } catch (PackageManager.NameNotFoundException e){
            dr = null;
        }
        return dr;
    }
}
