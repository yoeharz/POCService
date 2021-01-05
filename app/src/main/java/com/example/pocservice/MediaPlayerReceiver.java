package com.example.pocservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("aaa", "Masuk receiver");
        context.startService(new Intent(context, MediaPlayerService.class));
    }
}
