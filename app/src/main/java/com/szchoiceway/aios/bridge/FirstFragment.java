package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.clearLogData(getContext());
                ((MainActivity)getActivity()).updateTV();
            }
        });

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

        view.findViewById(R.id.rel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).updateTV();
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
        //TODO topListView.setOnItemClickListener(this);

        //Bottom app
        btmItem = getListFromPref(pm, Data.BOTTOM_APP);
        btmListView = view.findViewById(R.id.btmAppList);
        btmAdapter = new ApplicationListViewAdapter(getContext(), R.layout.selectedapp, btmItem);
        btmListView.setAdapter(btmAdapter);
        //TODO btmListView.setOnItemClickListener(this);
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

    private void removeFromBgPreference(String pkg){
        String bg = Data.getPreference(getContext(), Data.BACKGROUND_APPS);
        String upd = bg.replaceAll(pkg, "");
        upd = upd.replaceAll(";;", ";");//Replace consecutive semicolons with one.
        upd = upd.replaceAll(";$", "");//Remove extraneous semicolon at the end
        upd = upd.replaceAll("^;", "");//Remove extraneous semicolon at the beginning
        Data.setPreference(getContext(), Data.BACKGROUND_APPS, upd);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String toRemove = bgItems.get(position).getPkg();
        removeFromBgPreference(toRemove);
        bgAdapter.remove(bgAdapter.getItem(position));
        bgAdapter.notifyDataSetChanged();

    }
}
