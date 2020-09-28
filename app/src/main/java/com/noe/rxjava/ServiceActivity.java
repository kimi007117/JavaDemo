package com.noe.rxjava;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.android.core.util.TimeUtils;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.rxbus.RxBus;
import com.noe.rxjava.service.TestJobIntentService;
import com.noe.rxjava.service.TestService;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.work.TestWork;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lijie on 2020-04-30.
 */
@Route(path = ArouterUtils.ACTIVITY_SERVICE)
public class ServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startService(new Intent(mContext, TestService.class));
//                    }
//                }, 1000 * 3);
                startService(new Intent(mContext, TestService.class));
            }
        });
        findViewById(R.id.btn_click2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                },1000 * 65);
                long startTime = System.currentTimeMillis();
                Intent intent = new Intent();
                intent.putExtra("key1", "yyyyyyyyyy");
                TestJobIntentService.enqueueWork(mContext, intent);
                Logger.i("xxxxxxxx", TimeUtils.diffTime(startTime, System.currentTimeMillis()));
            }
        });

        findViewById(R.id.btn_click3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().postEmptyEvent("update");
            }
        });

        findViewById(R.id.btn_click4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,StartTestActivity.class));
                finish();
                finish();
            }
        });

        findViewById(R.id.btn_click5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                Data data = new Data.Builder().putString("time", dateFormat.format(new Date())).build();
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TestWork.class)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance().enqueue(request);
                WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                        .observe(ServiceActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo!=null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            String key_haha = workInfo.getOutputData().getString("key_haha");
                            Logger.i("ServiceActivity:",key_haha);
                        }
                    }
                });
            }
        });

    }
}
