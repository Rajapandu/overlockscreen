package com.example.overlockscreen;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.security.Provider;

public class LockScreenService extends Service {

    private ScreenReciver screenReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        screenReceiver = new ScreenReciver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Service is started
        return START_STICKY; // Ensure the service is restarted if killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
