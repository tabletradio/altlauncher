package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected BridgeApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.addData(getApplicationContext(),"MainActivity.onCreate()");

        app = (BridgeApplication) getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ApplicationInfo info = getApplicationContext().getApplicationInfo();
        boolean answer = (info.flags & 1) <= 0;
        Data.addData(getApplicationContext(),"MainActivity.create " + answer);
        Data.addData(getApplicationContext(),"MainActivity.create " + info.packageName);

        updateTV();

        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
    }

    public void updateTV(){
        TextView tv = findViewById(R.id.simple_text_view);
        tv.setText(Data.getData(getApplicationContext()));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStop() {
        super.onStop();
        Data.addData(getApplicationContext(),"MainActivity.onStop()");
    }

    public void onPause() {
        super.onPause();
        Data.addData(getApplicationContext(),"MainActivity.onPause()");
    }

    public void onResume() {
        super.onResume();
        Data.addData(getApplicationContext(),"MainActivity.onResume()");
    }

    public void onStart() {
        super.onStart();
        Data.addData(getApplicationContext(),"MainActivity.onStart()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.addData(getApplicationContext(),"MainActivity.onDestroy()");
    }
}
