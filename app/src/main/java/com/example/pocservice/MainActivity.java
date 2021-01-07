package com.example.pocservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start, stop;
    private Intent intentServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop); // tes commit

        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        /*intentServices = new Intent(this, MediaPlayerService.class);

        boolean isMyServiceRunning = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                isMyServiceRunning = true;
            }
        }

        if(isMyServiceRunning ==  false){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intentServices);
            }else{
                startService(intentServices);
            }
        }*/

    }

    @Override
    protected void onResume() {
        Log.i("aaa", "Masuk on resume");
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            boolean isMyServiceRunning = false;
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                    isMyServiceRunning = true;
                }
            }

            Log.i("aaa", "Masuk on resume: "+isMyServiceRunning);
            if(isMyServiceRunning ==  false){
                MediaPlayerReceiver.scheduleJob(getApplicationContext());
            }

        } else {
            ServiceAdmin bck = new ServiceAdmin();
            bck.launchService(getApplicationContext());
        }
    }

    @Override
    public void onClick(View view) {
        if(view == start){
            boolean isMyServiceRunning = false;
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                    isMyServiceRunning = true;
                }
            }

            if(isMyServiceRunning == false){
                startService(intentServices);
            }
        }else if(view == stop){
            boolean isMyServiceRunning = false;
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                    isMyServiceRunning = true;
                }
            }

            if(isMyServiceRunning == true){
                stopService(intentServices);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("aaa", "MAsuk on destroy main activity");
        boolean isMyServiceRunning = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                isMyServiceRunning = true;
            }
        }

        if(isMyServiceRunning == true){
            stopService(intentServices);
        }
        //stopService(intentServices);
        super.onDestroy();
    }
}