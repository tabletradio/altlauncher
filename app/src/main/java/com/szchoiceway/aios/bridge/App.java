package com.szchoiceway.aios.bridge;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.szchoiceway.aios.bridge.util.AppUtil.getAppActivityName;
import static com.szchoiceway.aios.bridge.util.AppUtil.getAppIcon;
import static com.szchoiceway.aios.bridge.util.AppUtil.getAppName;

public class App {
    public static final String PKG_ACT_SEP = ":";
    public static final String APP_SEP = ";";

    public static final App NONE_SELECTED = new App(null, "", "None Selected.", "", "");

    private Drawable image;
    private String name;
    private String pkg;
    private String service;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    private String activityName;

    public App(Drawable img, String pckage, String nm, String actName, String serviceName){
        this.image = img;
        this.name = nm;
        this.pkg = pckage;
        //this.name += actName;
        this.activityName = actName;
        this.service = serviceName;
    }

    public App(String prefString){
        if (null != prefString){
            String[] data = prefString.split(PKG_ACT_SEP);
            if (null != data){
                if (data.length > 0){
                    this.setPkg(data[0]);
                }
                if (data.length > 1){
                    this.setActivityName(data[1]);
                }
                if (data.length > 2){
                    this.setService(data[2]);
                }
            }
        }
    }

    public static List<App> getListFromPref(Context ctx, PackageManager pm, String prefName) {
        List<App> apps = new ArrayList<>();
        String all = Data.getPreference(ctx, prefName);
        if (null != all && all.length() > 0){
            String[] myApps = all.split(APP_SEP);
            if (null != myApps && myApps.length > 0) {
                for (String a : myApps) {
                    App tmp = new App(a);
                    if (null != tmp && null != tmp.getPkg()){
                        Drawable drw = getAppIcon(tmp.getPkg(), pm);
                        tmp.setImage(drw);
                        String name = getAppName(tmp.getPkg(), pm);
                        tmp.setName(name);
                        if (null == tmp.getActivityName()){
                            String actName = getAppActivityName(pm, tmp.getPkg());
                            tmp.setActivityName(actName);
                        }
                        apps.add(tmp);
                    }
                }
            }
        }
        if (apps.isEmpty()){
            apps.add(NONE_SELECTED);
        }
        return apps;
    }

    public String getPrefString(){
        return this.getPkg() + PKG_ACT_SEP + this.getActivityName() + PKG_ACT_SEP + this.getService();
    }

    public static void addToBgPreference(Context ctx, App app){
        if (null != app && null != app.getPrefString()) {
            String bg = Data.getPreference(ctx, Data.BACKGROUND_APPS);
            if (bg.length() > 1) {
                bg = bg + APP_SEP;
            }
            bg = bg + app.getPrefString();
            Data.setPreference(ctx, Data.BACKGROUND_APPS, bg);
            Data.addLogData(ctx, "App.addToBgPreference updated pref = " + bg);
        } else {
            Data.addLogData(ctx, "App.addToBgPreference called with empty App.");
        }
    }

    public static void addToServicePreference(Context ctx, App app){
        if (null != app && null != app.getPrefString()) {
            String services = Data.getPreference(ctx, Data.SERVICE_APPS);
            if (services.length() > 1){
                services = services + APP_SEP;
            }
            services = services + app.getPrefString();
            Data.setPreference(ctx, Data.SERVICE_APPS, services);
            Data.addLogData(ctx, "App.addToServicePreference updated pref = " + services);
        } else {
            Data.addLogData(ctx, "App.addToServicePreference called with empty App.");
        }
    }

    public static void removeFromBgPreference(Context ctx, App app){
        if (null != app && null != app.getPrefString()) {
            String fullName = app.getPrefString();
            String name = app.getPkg();
            String bgPref = Data.getPreference(ctx, Data.BACKGROUND_APPS);
            if (null != bgPref && bgPref.length() > 0) {
                String[] bgApps = bgPref.split(APP_SEP);
                List<String> tmp = new ArrayList<>();
                boolean found = false;
                if (null != bgApps && bgApps.length > 0){
                    //Apps in the preference can be either "package:activity" or
                    // "package". Be sure to check for all of them.
                    for (String bgApp : bgApps){
                        //Maches either package:activity or just the package
                        if (fullName.equals(bgApp) || name.equals(bgApp)){
                            found = true;
                        } else {
                            tmp.add(bgApp);
                        }
                    }
                    if (found){
                        bgPref = String.join(APP_SEP, tmp);
                        Data.addLogData(ctx, "App.removeFromBgPreference updated pref = " + bgPref);
                    } else {
                        //This is a problem.
                        Data.addLogData(ctx, "App.removeFromBgPreference 1 unable to remove app from bg pref.");
                        Data.addLogData(ctx, "App.removeFromBgPreference 2 bg pref = " + bgPref);
                        Data.addLogData(ctx, "App.removeFromBgPreference 3 app = " + fullName);
                        bgPref = "";
                    }
                    Data.setPreference(ctx, Data.BACKGROUND_APPS, bgPref);
                }
            } else {
                Data.addLogData(ctx, "App.removeFromBgPreference called when preference is empty.");
            }
        } else {
            Data.addLogData(ctx, "App.removeFromBgPreference called with null/empty App.");
        }
    }

    public static void removeFromServicePreference(Context ctx, App app){
        if (null != app && null != app.getPrefString()) {
            String fullName = app.getPrefString();
            String name = app.getPkg();
            String servicePref = Data.getPreference(ctx, Data.SERVICE_APPS);
            if (null != servicePref && servicePref.length() > 0) {
                String[] servApps = servicePref.split(APP_SEP);
                List<String> tmp = new ArrayList<>();
                boolean found = false;
                if (null != servApps && servApps.length > 0){
                    //Apps in the preference can be either "package:activity:service" or
                    // "package:activity" or "package". Be sure to check for all of them.
                    for (String servApp : servApps){
                        //Maches either package:activity or just the package
                        if (fullName.equals(servApp) || name.equals(servApp)){
                            found = true;
                        } else {
                            tmp.add(servApp);
                        }
                    }
                    if (found){
                        servicePref = String.join(APP_SEP, tmp);
                        Data.addLogData(ctx, "App.removeFromServicePreference updated pref = " + servicePref);
                    } else {
                        //This is a problem.
                        Data.addLogData(ctx, "App.removeFromServicePreference 1 unable to remove app from bg pref.");
                        Data.addLogData(ctx, "App.removeFromServicePreference 2 bg pref = " + servicePref);
                        Data.addLogData(ctx, "App.removeFromServicePreference 3 app = " + fullName);
                        servicePref = "";
                    }
                    Data.setPreference(ctx, Data.SERVICE_APPS, servicePref);
                }
            } else {
                Data.addLogData(ctx, "App.removeFromServicePreference called when preference is empty.");
            }
        } else {
            Data.addLogData(ctx, "App.removeFromServicePreference called with null/empty App.");
        }
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable img) {
        this.image = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String serv){
        this.service = serv;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return pkg.equals(app.pkg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkg);
    }
}
