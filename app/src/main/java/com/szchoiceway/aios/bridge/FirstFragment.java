package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView bgListView;
    List<App> bgItems;
    ApplicationListViewAdapter bgAdapter;

    ListView topListView;
    List<App> topItem;
    ApplicationListViewAdapter topAdapter;

    ListView btmListView;
    List<App> btmItem;
    ApplicationListViewAdapter btmAdapter;

    ListView allListView;
    List<App> allItems;
    ApplicationListViewAdapter allAdapter;

    View rootView;
    LayoutInflater inf;
    private AppType currentAppType;
    PopupWindow popup;

    private static final App noneSelected = new App(null, "", "None Selected.");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        inf = inflater;
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_first, container, false);
        currentAppType = AppType.NONE;
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button launchButton = view.findViewById(R.id.launch);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.addLogData(getContext(), "Launch button clicked.");
                Intent in = new Intent(view.getContext(), DaemonService.class);
                in.putExtra(DaemonService.LAUNCH_APPS, true);
                in.putExtra(DaemonService.SCREEN_TURN_ON, false);
                view.getContext().startForegroundService(in);
            }
        });

        view.findViewById(R.id.newAppBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentAppType = AppType.APP_BG;
                //Brings up the dialog to add a new background app to launch.
                showAll(v, bgItems);
            }
        });

        PackageManager pm = getContext().getPackageManager();

        //Background items
        bgItems = getListFromPref(pm, Data.BACKGROUND_APPS);
        bgListView = view.findViewById(R.id.backgroundAppList);
        bgAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, bgItems);
        bgListView.setAdapter(bgAdapter);
        bgListView.setOnItemClickListener(this);

        //Top app
        topItem = getListFromPref(pm, Data.TOP_APP);
        topListView = view.findViewById(R.id.topAppList);
        topAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, topItem);
        topListView.setAdapter(topAdapter);
        topListView.setOnItemClickListener(this);

        //Bottom app
        btmItem = getListFromPref(pm, Data.BOTTOM_APP);
        btmListView = view.findViewById(R.id.btmAppList);
        btmAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, btmItem);
        btmListView.setAdapter(btmAdapter);
        btmListView.setOnItemClickListener(this);
    }

    private void setApp(AppType type, App app){
        PackageManager pm = getContext().getPackageManager();
        if (type == AppType.APP_TOP){
            Data.setPreference(getContext(), Data.TOP_APP, app.getPkg());
            topAdapter.remove(topAdapter.getItem(0));
            topAdapter.add(app);
            topAdapter.notifyDataSetChanged();
        } else if (type == AppType.APP_BTM){
            Data.setPreference(getContext(), Data.BOTTOM_APP, app.getPkg());
            btmAdapter.remove(btmAdapter.getItem(0));
            btmAdapter.add(app);
            btmAdapter.notifyDataSetChanged();
        } else if (type == AppType.APP_BG){
            if (!app.getPkg().equals("")){
                bgAdapter.remove(noneSelected);
            }
            addToBgPreference(app.getPkg());
            bgAdapter.add(app);
            bgAdapter.notifyDataSetChanged();
        }
    }

    private List<App> getListFromPref(PackageManager pm, String prefName) {
        List<App> apps = new ArrayList<>();
        String all = Data.getPreference(getContext(), prefName);
        if (null != all && all.length() > 0){
            String[] myApps = all.split(";");
            if (null != myApps && myApps.length > 0) {
                for (String a : myApps) {
                    Drawable drw = getAppIcon(a, pm);
                    String name = getAppName(a, pm);
                    App tmp = new App(drw, a, name);
                    apps.add(tmp);
                }
            }
        }
        if (apps.isEmpty()){
            apps.add(noneSelected);
        }
        return apps;
    }

    private String getAppName(String pkg, PackageManager pm) {
        String nm = pkg;
        try {
            CharSequence cs = pm.getApplicationLabel(pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA));
            nm = cs.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        };
        return nm;
    }

    private Drawable getAppIcon(String pkg, PackageManager pm){
        Drawable dr;
        try {
            dr = pm.getApplicationIcon(pkg);
        } catch (PackageManager.NameNotFoundException e){
            dr = null;
        }
        return dr;
    }

    private List<App> getAllApps(){
        PackageManager pm = getContext().getPackageManager();
        List<App> all = new ArrayList<>();
        all.add(noneSelected);
        List<PackageInfo> pkgs = pm.getInstalledPackages(0);
        if (null != pkgs && !pkgs.isEmpty()){
            for (PackageInfo pf : pkgs){
                ApplicationInfo inf = pf.applicationInfo;
                if (null != inf && null != inf.name){
                    String nm = getAppName(inf.packageName, pm);
                    App tmp = new App(getAppIcon(inf.packageName, pm), inf.packageName, nm);
                    all.add(tmp);
                }
            }
        }
        return all;
    }

    private void addToBgPreference(String pkg){
        String bg = Data.getPreference(getContext(), Data.BACKGROUND_APPS);
        if (bg.length() > 1){
            bg = bg + ";";
        }
        bg = bg + pkg;
        Data.setPreference(getContext(), Data.BACKGROUND_APPS, bg);
    }

    private void removeFromBgPreference(String pkg){
        String bg = Data.getPreference(getContext(), Data.BACKGROUND_APPS);
        String upd = bg.replaceAll(pkg, "");
        upd = upd.replaceAll(";;", ";");//Replace consecutive semicolons with one.
        upd = upd.replaceAll(";$", "");//Remove extraneous semicolon at the end
        upd = upd.replaceAll("^;", "");//Remove extraneous semicolon at the beginning
        Data.setPreference(getContext(), Data.BACKGROUND_APPS, upd);
    }

    //https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
    private void showAll(View view, List<App> existing){
        //Inflate the layout of the popup
        View popupView = inf.inflate(R.layout.all_apps, null);

        //Bottom app
        PackageManager pm = getContext().getPackageManager();
        allItems = getAllApps();
        if (null != existing && !existing.isEmpty()){
            for (App a : existing){
                allItems.remove(a);
            }
        }
        allListView = popupView.findViewById(R.id.allListView);
        allAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, allItems);
        allListView.setAdapter(allAdapter);
        allListView.setOnItemClickListener(this);

        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; //Let taps outside dismiss it
        popup = new PopupWindow(popupView, 900, 1600, focusable);

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popup.setElevation(5.0f);
        }

        //show the window
        popup.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Remove from the background list
        if (parent.getId() == R.id.backgroundAppList) {
            currentAppType = AppType.NONE;
            String toRemove = bgItems.get(position).getPkg();
            if (!toRemove.equals("")) {
                removeFromBgPreference(toRemove);
                bgAdapter.remove(bgAdapter.getItem(position));
                if (bgAdapter.isEmpty()) {
                    bgAdapter.add(noneSelected);
                }
                bgAdapter.notifyDataSetChanged();
            }
        }

        //No app type selected = bring up the list of apps to choose from.
        if (currentAppType == AppType.NONE) {
            //Time to select the bottom app
            if (parent.getId() == R.id.btmAppList) {
                currentAppType = AppType.APP_BTM;
                showAll(view, btmItem);
                System.out.println("bottom");
            } else if (parent.getId() == R.id.topAppList) { //Select the top app.
                currentAppType = AppType.APP_TOP;
                showAll(view, topItem);
                System.out.println("top");
            }
        } else {//Current app type set - user selected an app
            if (parent.getId() == R.id.allListView){//User clicked an app in the popup
                if (currentAppType == AppType.APP_TOP){//Setting the Top app
                    App tmp = allAdapter.getItem(position);
                    setApp(AppType.APP_TOP, tmp);
                    if (null != popup){
                        popup.dismiss();
                        popup = null;
                    }
                } else if (currentAppType == AppType.APP_BTM) {//Setting the Bottom app
                    App tmp = allAdapter.getItem(position);
                    setApp(AppType.APP_BTM, tmp);
                    if (null != popup){
                        popup.dismiss();
                        popup = null;
                    }
                } else if (currentAppType == AppType.APP_BG) {//Adding to the Background apps.
                    App tmp = allAdapter.getItem(position);
                    setApp(AppType.APP_BG, tmp);
                    if (null != popup){
                        popup.dismiss();
                        popup = null;
                    }
                }
            }
            currentAppType = AppType.NONE;
        }
    }

    public enum AppType {
        NONE,
        APP_BG,
        APP_TOP,
        APP_BTM
    }
}
