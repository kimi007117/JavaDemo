package com.noe.rxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by 58 on 2016/8/24. eventbus 3.0
 */
public class EventBusActivity extends AppCompatActivity {

    private Button btnEvent;
    private TextView mTextView;
    private Context mContext;

    Toast mToast;
    int mCounter;

    final Handler mHandler = new Handler();

    final Runnable mUpdateToast = new Runnable() {
        @Override
        public void run() {
            String text = String.format("Counter is: %d", mCounter++);
            mToast.setText(text);

            Log.d("CRASH", text);
            mToast.show();
            mHandler.post(mUpdateToast);
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_event_bus);
        btnEvent = (Button) findViewById(R.id.btnEvent);
        mTextView = (TextView) findViewById(R.id.text);
//        observer();
//        subscriber();
        mToast = Toast.makeText(mContext, null, Toast.LENGTH_LONG);

        mUpdateToast.run();

        btnEvent.setOnClickListener(v -> {
//            SimpleToast.showToast(mContext.getApplicationContext(),"哈哈哈哈哈哈哈");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    SimpleToast.showToast(mContext.getApplicationContext(),"哈哈哈哈哈哈哈");
//                }
//            }).start();

            EventBus.getDefault().post("hello");});


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void observer() {

        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
//                Toast.makeText(mContext.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello event");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(observer);
        Observable<String> ss = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("ss");
            }
        });
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        });
        Observable.concat(ss,stringObservable).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });
        Observable.zip(ss, stringObservable, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return null;
            }
        });


    }

    private void subscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("event", s);
            }

        };
        List<String> list = new ArrayList<>();
        list.add("ssss");
        list.add("aaaa");
        Observable<String> observable = Observable.just("Hello", "Hi", "Aloha");
        Observable<String> observable1 = Observable.from(list);
//        observable.subscribe(subscriber);
        observable1.subscribe(subscriber);
        Observable<String> o = Observable.just("Hello", "Hi", "Aloha");

    }


    private void getBitmap() {
        Observable.just("http://ww3.sinaimg.cn/mw1024/52eb2279jw1f2rx4ddcncj2046053gll.jpg")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {

                    }
                });

    }

    private void flatMap(){
        List<String> list = new ArrayList<>();
        list.add("ssss");
        list.add("aaaa");
        Observable.from(list)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return null;
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });
    }
}
