package com.noe.rxjava.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by lijie on 2020-05-06.
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TestJobService.this, "xxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
        },3000);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
