package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.szchoiceway.aios.bridge.service.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    protected BridgeApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.addLogData(getApplicationContext(),"MainActivity.onCreate()");

        app = (BridgeApplication) getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ApplicationInfo info = getApplicationContext().getApplicationInfo();
        boolean answer = (info.flags & 1) <= 0;
        Data.addLogData(getApplicationContext(),"MainActivity.create " + answer);
        Data.addLogData(getApplicationContext(),"MainActivity.create " + info.packageName);

        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStop() {
        super.onStop();
        Data.addLogData(getApplicationContext(),"MainActivity.onStop()");
    }

    public void onPause() {
        super.onPause();
        Data.addLogData(getApplicationContext(),"MainActivity.onPause()");
    }

    public void onResume() {
        super.onResume();
        Data.addLogData(getApplicationContext(),"MainActivity.onResume()");
    }

    public void onStart() {
        super.onStart();
        Data.addLogData(getApplicationContext(),"MainActivity.onStart()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.addLogData(getApplicationContext(),"MainActivity.onDestroy()");
    }
}
