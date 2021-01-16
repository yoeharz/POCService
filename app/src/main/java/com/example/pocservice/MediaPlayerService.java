package com.example.pocservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class MediaPlayerService extends Service {

    private MediaPlayer player;
    int xKey = 0;

    @Override
    public void onCreate() {
        //startForegroundService(new Intent(this, MediaPlayerService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("aaa","Masuk start services");

        xKey = intent.getIntExtra("keyx",0);

        if(xKey == 1) {
            stopForeground(true);
            stopSelf();
        }else{
            player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
            player.setLooping(true);
            player.start();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("aaa","Masuk destroy services");
        if(xKey == 0) {
            Intent broadcastIntent = new Intent(this, MediaPlayerReceiver.class);
            sendBroadcast(broadcastIntent);
        }
        if(player != null){ player.stop();}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("aaa", "masuk stop foreground: "+xKey);
            stopForeground(true);
            if(xKey == 1) {
                xKey = 0;
                stopSelf();
            }
        } else {
            Log.i("aaa", "masuk stop self");
            stopSelf();
        }

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("aaa","Masuk onTaskRemoved services");
        super.onTaskRemoved(rootIntent);
    }
}
