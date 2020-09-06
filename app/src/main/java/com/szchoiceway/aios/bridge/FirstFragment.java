package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.szchoiceway.aios.bridge.util.AppUtil.getAppIcon;
import static com.szchoiceway.aios.bridge.util.AppUtil.getAppName;

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
        bgItems = App.getListFromPref(getContext(), pm, Data.BACKGROUND_APPS);
        bgListView = view.findViewById(R.id.backgroundAppList);
        bgAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, bgItems);
        bgListView.setAdapter(bgAdapter);
        bgListView.setOnItemClickListener(this);

        //Top app
        topItem = App.getListFromPref(getContext(), pm, Data.TOP_APP);
        topListView = view.findViewById(R.id.topAppList);
        topAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, topItem);
        topListView.setAdapter(topAdapter);
        topListView.setOnItemClickListener(this);

        //Bottom app
        btmItem = App.getListFromPref(getContext(), pm, Data.BOTTOM_APP);
        btmListView = view.findViewById(R.id.btmAppList);
        btmAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, btmItem);
        btmListView.setAdapter(btmAdapter);
        btmListView.setOnItemClickListener(this);
    }

    private void setApp(AppType type, App app){
        PackageManager pm = getContext().getPackageManager();
        if (type == AppType.APP_TOP){
            Data.setPreference(getContext(), Data.TOP_APP, app.getPrefString());
            topAdapter.remove(topAdapter.getItem(0));
            topAdapter.add(app);
            topAdapter.notifyDataSetChanged();
            Data.addLogData(getContext(), "FirstFragment.setApp. Set top app to " + app.getPrefString());
        } else if (type == AppType.APP_BTM){
            Data.setPreference(getContext(), Data.BOTTOM_APP, app.getPrefString());
            btmAdapter.remove(btmAdapter.getItem(0));
            btmAdapter.add(app);
            btmAdapter.notifyDataSetChanged();
            Data.addLogData(getContext(), "FirstFragment.setApp. Set bottom app to " + app.getPrefString());
        } else if (type == AppType.APP_BG){
            if (!app.getPkg().equals("")){
                bgAdapter.remove(App.NONE_SELECTED);
            }
            App.addToBgPreference(getContext(), app);
            bgAdapter.add(app);
            bgAdapter.notifyDataSetChanged();
        }
    }





    private List<App> getAllApps(){
        PackageManager pm = getContext().getPackageManager();
        List<App> all = new ArrayList<>();
        all.add(App.NONE_SELECTED);
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = pm.queryIntentActivities(main,0);
        Collections.sort(infos, new ResolveInfo.DisplayNameComparator(pm));

        if (null != infos && !infos.isEmpty()){
            for (ResolveInfo inf : infos){
                if (null != inf){
                    String nm = getAppName(inf.activityInfo.packageName, pm);
                    App tmp = new App(getAppIcon(inf.activityInfo.packageName, pm), inf.activityInfo.packageName, nm,
                            inf.activityInfo.name);
                    all.add(tmp);
                }
            }
        }
        //List<PackageInfo> pkgs = pm.getInstalledPackages(0);
        //if (null != pkgs && !pkgs.isEmpty()){
        //    for (PackageInfo pf : pkgs){
        //        ApplicationInfo inf = pf.applicationInfo;
        //        if (null != inf && null != inf.name){
        //            String nm = getAppName(inf.packageName, pm);
        //            App tmp = new App(getAppIcon(inf.packageName, pm), inf.packageName, nm);
        //            all.add(tmp);
        //        }
        //    }
        //}
        return all;
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
            App toRemove = bgItems.get(position);
            if (null != toRemove && null != toRemove.getPrefString() && !toRemove.getPrefString().equals("")) {
                App.removeFromBgPreference(getContext(), toRemove);
                bgAdapter.remove(bgAdapter.getItem(position));
                if (bgAdapter.isEmpty()) {
                    bgAdapter.add(App.NONE_SELECTED);
                }
                bgAdapter.notifyDataSetChanged();
            } else {
                Data.addLogData(getContext(), "FirstFragment.onIemClick remove bg called with empty App.");
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
