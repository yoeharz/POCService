package com.example.pocservice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

public class MediaPlayerReceiver extends BroadcastReceiver {

    private static JobScheduler jobScheduler;
    private MediaPlayerReceiver mediaPlayerReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("aaa", "Masuk receiver");
        //context.startService(new Intent(context, MediaPlayerService.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.i("aaa", "Build version diatas atau sama dengan lolipop");
            scheduleJob(context);
        }else{
            Log.i("aaa", "Build version dibawah lolipop");
            registerRestarterReceiver(context);
            ServiceAdmin bck = new ServiceAdmin();
            bck.launchService(context);
        }

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("aaa", "Masuk setelah boot");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Log.i("aaa", "Build version diatas atau sama dengan lolipop");
                scheduleJob(context);
            }else{
                Log.i("aaa", "Build version dibawah lolipop");
                registerRestarterReceiver(context);
                ServiceAdmin bck = new ServiceAdmin();
                bck.launchService(context);
            }
        }
    }

    public static void scheduleJob(Context context){
        if(jobScheduler == null){
            jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }

        ComponentName componentName = new ComponentName(context, JobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setOverrideDeadline(0).setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }

    private void registerRestarterReceiver(final Context context) {

        // the context can be null if app just installed and this is called from restartsensorservice
        // https://stackoverflow.com/questions/24934260/intentreceiver-components-are-not-allowed-to-register-to-receive-intents-when
        // Final decision: in case it is called from installation of new version (i.e. from manifest, the application is
        // null. So we must use context.registerReceiver. Otherwise this will crash and we try with context.getApplicationContext
        if (mediaPlayerReceiver == null)
            mediaPlayerReceiver = new MediaPlayerReceiver();
        else try{
            context.unregisterReceiver(mediaPlayerReceiver);
        } catch (Exception e){
            // not registered
        }
        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // we register the  receiver that will restart the background service if it is killed
                // see onDestroy of Service
                IntentFilter filter = new IntentFilter();
                filter.addAction(Globals.RESTART_INTENT);
                try {
                    context.registerReceiver(mediaPlayerReceiver, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(mediaPlayerReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }
}
