package com.example.pocservice;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {

    private static String TAG = "JobService";
    private static MediaPlayerReceiver mediaPlayerReceiver;
    private static JobService instance;
    private static JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("aaa", "Masuk onstart job");
        ServiceAdmin serviceAdmin = new ServiceAdmin();
        serviceAdmin.launchService(this);
        instance = this;
        JobService.jobParameters = jobParameters;
        return false;
    }

    private void registerRestarterReceiver() {
        if(mediaPlayerReceiver == null){
            mediaPlayerReceiver = new MediaPlayerReceiver();
        }else try{
            unregisterReceiver(mediaPlayerReceiver);
        }catch (Exception e){
            Log.i("aaa", "not registered");
            e.printStackTrace();
        }

        //give the time to run
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Globals.RESTART_INTENT);
                try {
                    registerReceiver(mediaPlayerReceiver, filter);
                }catch (Exception e){
                    try {
                        getApplicationContext().registerReceiver(mediaPlayerReceiver, filter);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        },1000);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("aaa", "Stoping job");
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);

        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                unregisterReceiver(mediaPlayerReceiver);
                stopJob();
            }
        },1000);
        return false;
    }

    /**
     * called when the tracker is stopped for whatever reason
     * @param
     */
    public static void stopJob(){
        if(instance != null && jobParameters != null){
            try {
                instance.unregisterReceiver(mediaPlayerReceiver);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.i("aaa", "Finishing job");
            instance.jobFinished(jobParameters, true);
        }
    }
}
