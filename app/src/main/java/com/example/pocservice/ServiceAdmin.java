package com.example.pocservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ServiceAdmin {

    private static Intent serviceIntent = null;

    public ServiceAdmin() {
    }

    private void setServiceIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, MediaPlayerService.class);
        }
    }

    public void launchService(Context context) {
        if (context == null) {
            return;
        }
        setServiceIntent(context);

        // depending on the version of Android we either launch the simple service (version<O)
        // or we start a foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("aaa", "launchService:  Service is starting on foreground....");
            //context.startService(serviceIntent);
            boolean isMyServiceRunning = false;
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(MediaPlayerService.class.getName().equals(service.service.getClassName())){
                    Log.i("aaa", service.service.getClassName());
                    isMyServiceRunning = true;
                }
            }

            Log.i("aaa", "isMyServiceRunning: "+isMyServiceRunning);
            if(isMyServiceRunning == false){
                //context.startService(serviceIntent);
                context.startForegroundService(serviceIntent);
            }
        } else {
            Log.d("aaa", "launchService:  Service is starting on background....");
            //context.startForegroundService(serviceIntent);
            context.startService(serviceIntent);
        }
        Log.d("aaa", "launchService:  Service is starting....");
    }

    public void stopService(Context context){
        Log.i("aaa", "Sempet masuk stop services");
        setServiceIntent(context);
        context.stopService(serviceIntent);
    }
}
