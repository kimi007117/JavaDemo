package com.noe.rxjava.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;

import com.android.core.util.Logger;


/**
 * Created by lijie on 2020-04-28.
 */
public class TestService extends Service {

    private static final String TAG = TestService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(1, new Notification()); // 不是广播通知
//            Logger.i("TestService", "yyyyyyyy");
//        }
        Logger.i(TAG,  " --> " + Thread.currentThread());
        Logger.i(TAG,"onCreate XXXXXXXXXXXXXXX " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
