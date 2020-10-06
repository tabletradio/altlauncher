package com.szchoiceway.aios.bridge.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szchoiceway.aios.bridge.Data;
import com.szchoiceway.aios.bridge.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;

public class SettingsActivity extends AppCompatActivity {

    static LayoutInflater inf;
    static PopupWindow popup;
    static long lastScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        inf = getLayoutInflater();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference pref = findPreference("delay_after_app_launch");
            setListener(pref, false);

            pref = findPreference("delay_after_fast_boot");
            setListener(pref, false);

            pref = findPreference("delay_after_boot");
            setListener(pref, true);

            pref = findPreference("delay_after_service_launch");
            setListener(pref, true);

            Preference send = findPreference("send_log");
            send.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return false;
                }
            });

            Preference about = findPreference("about");
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    about(getActivity(), getView());
                    return false;
                }
            });

            Preference logs = findPreference("view_log");
            logs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    logs(getActivity(), getView());
                    return false;
                }
            });
        }
    }

    private static void setListener(EditTextPreference pref, boolean allowZero){
        pref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });
        if (allowZero){
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (null != newValue) {
                        try {
                            long value = Long.parseLong((String) newValue);
                            if (value >= 0) {
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    return false;
                }
            });
        } else {
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (null != newValue) {
                        try {
                            long value = Long.parseLong((String) newValue);
                            if (value > 0) {
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public static void logs(Context ctx, View vw){
        lastScroll = 0;
        //Inflate the layout of the popup
        View popupView = inf.inflate(R.layout.about, null);

        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; //Let taps outside dismiss it
        //popup = new PopupWindow(popupView, 1900, 5600, focusable);
        popup = new PopupWindow(popupView, width, height, focusable);

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popup.setElevation(5.0f);
        }

        //show the window
        popup.showAtLocation(vw, Gravity.CENTER, 0, 0);

        AppCompatTextView txt = popupView.findViewById(R.id.about_text_view);
        txt.setText(Data.getLogData(ctx));

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lastScroll > 500){
                            popup.dismiss();
                            return true;
                        }
                        System.out.println("up");
                        break;
                    case MotionEvent.ACTION_SCROLL:
                        lastScroll = System.currentTimeMillis();
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        lastScroll = System.currentTimeMillis();
                        return false;
                }
                return false;
            }
        });
    }

    public static void about(Context ctx, View view){
        //Inflate the layout of the popup
        View popupView = inf.inflate(R.layout.about, null);

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
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup.dismiss();
                return true;
            }
        });
    }

    public static void sendFeedback(Context context){
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER +
                    "\n Logs: " + Data.getLogData(context);
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tabletradio@protonmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Logs from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}