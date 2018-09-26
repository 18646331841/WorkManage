package com.barisetech.www.workmanage.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.barisetech.www.workmanage.utils.LogUtil;

/**
 * Created by LJH on 2018/9/25.
 */
public class MyNotifyService extends Service {
    private static final String TAG = "MyNotifyService";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate");
        startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //5.0以下保活
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
        gcEnv();
    }

    /**
     * 5.0以下保活
     */
    private void gcEnv() {
        Intent serviceTo = new Intent(getApplicationContext(), MyNotifyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceTo);
        } else {
            startService(serviceTo);
        }
    }
}
