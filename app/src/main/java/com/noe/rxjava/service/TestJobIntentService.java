package com.noe.rxjava.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import android.widget.Toast;

import com.android.core.context.ContextProvider;
import com.android.core.util.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lijie on 2020-04-30.
 */
public class TestJobIntentService extends JobIntentService {
    private static final String TAG = TestJobIntentService.class.getSimpleName();


    private static final int JOB_ID = 1000;

    private Handler handler = new MyHandler();

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, TestJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.i("TestJobIntentService", intent.getStringExtra("key1") + " --> " + Thread.currentThread());
        Logger.i(TAG,"onHandleWork XXXXXXXXXXXXXXX " + (Thread.currentThread() == Looper.getMainLooper().getThread()));

//        RxBus.getInstance().toObservableOnMain("update").subscribe(new Action1<Event>() {
//            @Override
//            public void call(Event event) {
//                Logger.i("TestJobIntentService","toObservableOnMain");
//            }
//        });
//        download("http://10.136.199.8/package/92099/58zhaopin_v56.1.0_S-on_WAPM-off-debug.apk");

        Message msg1 = Message.obtain();
        msg1.what = 11;
        handler.sendMessage(msg1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("TestJobIntentService","onDestroy");
    }


    public static void download(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                Logger.i("TestJobIntentService","download failed --> " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                is = response.body().byteStream();
                long total = response.body().contentLength();

                try {
                    while ((len = is.read(buf)) != -1){
                        Logger.i("TestJobIntentService","response --> " + len);
                    }
                } catch (Exception e){

                }

                Logger.i("TestJobIntentService","response --> " + response.toString());
            }
        });
    }


    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContextProvider.get().getContext(), "xxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
                        }
                    },3000);
                    break;
            }
        }
    }

}
