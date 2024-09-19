package com.example.overlockscreen;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private  ScreenReciver screenreciver;
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       screenreciver = new ScreenReciver();
        checkAndRequestOverlayPermission();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);

        checkIfDeviceUnlocked();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Check again when the activity resumes (e.g., user may have unlocked the device)
        checkIfDeviceUnlocked();
    }

    private void checkAndRequestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }
    }

    private void checkIfDeviceUnlocked() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (!keyguardManager.isKeyguardLocked()) {
            // Device is unlocked, close the activity
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                checkIfDeviceUnlocked();
            } else {
                // Permission denied
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenreciver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(screenreciver);
    }
}