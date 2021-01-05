package com.example.pocservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class MediaPlayerService extends Service {

    private MediaPlayer player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("aaa","Masuk start services");
        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.setLooping(true);
        player.start();
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
        Intent broadcastIntent = new Intent(this, MediaPlayerReceiver.class);
        sendBroadcast(broadcastIntent);
        if(player != null){ player.stop();}

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("aaa","Masuk onTaskRemoved services");
        super.onTaskRemoved(rootIntent);
    }
}
