package com.noe.rxjava.work;


import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.core.util.Logger;


/**
 * Created by lijie on 2020-05-08.
 */
public class TestWork extends Worker {

    private static final String TAG = TestWork.class.getSimpleName();

    public TestWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String time = getInputData().getString("time");
        Logger.i(TAG,"time zzzzz " + time);

        Data data = new Data.Builder().putString("key_haha", time).build();


        Logger.i(TAG,"doWork yyyyyyyyyyyy" + Thread.currentThread());
        Logger.i(TAG,"doWork XXXXXXXXXXXXXXX" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
        return Result.success(data);
    }
}
