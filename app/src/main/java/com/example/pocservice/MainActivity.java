package com.example.pocservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start, stop, cancelJob;
    private Intent intentServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*new ServiceAdmin().stopService(this);*/

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        cancelJob = (Button) findViewById(R.id.cancelJob);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);


        Intent xIntent = new Intent(this, MediaPlayerService.class);
        xIntent.putExtra("keyx",1);
        startService(xIntent);
        boolean x = stopService(xIntent);
        Log.i("xxx", "x = "+x);
        //JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        //jobScheduler.cancel(123);
        Log.i("aaa","execute service 2");
        Intent xIntent2 = new Intent(this, MediaPlayerService.class);
        xIntent2.putExtra("keyx",0);
        startService(xIntent2);
        Log.i("aaa","selesai execute service 2");
    }

    @Override
    protected void onResume() {
        //Log.i("xxx")
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if(view == start){
            intentServices = new Intent(this, MediaPlayerService.class);
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
        }else if(view == cancelJob){
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.cancel(123);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("aaa", "MAsuk on destroy main activity");
        intentServices = new Intent(this, MediaPlayerService.class);
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