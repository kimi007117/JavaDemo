package com.noe.rxjava;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.base.RxApplication;
import com.noe.rxjava.bean.HotWordBean;
import com.noe.rxjava.bean.TopicListBean;
import com.noe.rxjava.data.ApiService;
import com.noe.rxjava.data.RetroFactory;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.util.ScrimUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private ImageView iv;
    private ListView mListView;
    private View headerView;

    private int mMaxHeight;
    private TextView mTitle;

    private Button mButton;
    private Button buttonBar;
    private Button btnSecond;
    private final static String baidu = "https://api.douban.com/v2/book/1084336";

    private int time = 5;
    private Timer timer;

    private StaticHandler mStaticHandler = new StaticHandler(this);


    final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 处理一些手机二维码安装后直接点击打开，home键后再进来还走launch的问题，这句代码应该是在launch页的，因为没有launch页就只能放这了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions &= ~(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        initView();
        initData();
        Log.i("Life", "onCreate");


    }

    private void initView() {
        mMaxHeight = (int) getResources().getDimension(R.dimen.titlebar_height);
        headerView = getLayoutInflater().inflate(R.layout.item_header, null);
        mTextView = (TextView) headerView.findViewById(R.id.text1);
        iv = (ImageView) headerView.findViewById(R.id.iv);
        mTitle = (TextView) findViewById(R.id.title1);
        mListView = (ListView) findViewById(R.id.listView);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DragHelperActivity.class)));
        buttonBar = (Button) findViewById(R.id.buttonBar);
        btnSecond = (Button) findViewById(R.id.buttonSecond);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btnSecond.setBackground(ScrimUtil.makeCubicGradientScrimDrawable(getResources().getColor(R.color.colorPrimary), //颜色
                    8, //渐变层数
                    Gravity.BOTTOM));
        }

        buttonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, BarActivity.class));
                ARouter.getInstance().build(ArouterUtils.ACTIVITY_BAR).navigation();
            }
        });

        ((Button) findViewById(R.id.buttonClip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClipActivity.class));
            }
        });

        btnSecond.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            ARouter.getInstance().build(ArouterUtils.ACTIVITY_SECOND).navigation();
        });

        String s = "\"filter\":{\"GetMajorCategoryFilter\":{\"categoryId\":\"2\",\"majorCategoryScriptIndex\": \"-2\"}},\"query\":{\"SearchPostsByJson2\":{\"categoryId\":\"2\",\"majorCategoryScriptIndex\":\"-2\"}}";
        Log.i("xxxxxxxxxxxxxxxxxxxxxxx", s);
        try {
            JSONObject jsonObject = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        timer = new Timer();
//
//        timer.schedule(task, 0, 1000);
        new AdCountDownTimer(3000, 1000).start();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    mTitle.setText(time + " 秒");
                    time--;
                    if (time < 0) {
                        timer.cancel();
                        mTitle.setText(0 + " 秒");
                    }
                }
            });
        }
    };

    private class AdCountDownTimer extends CountDownTimer {

        AdCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTitle.setText(String.format("%s 跳过", millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            this.cancel();
        }
    }

    private void initData() {
        new Thread(() -> {
//            httpGet(baidu);

        }).start();
        /**
         Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("hello");
        }
        });

         Subscriber<String> mSubscriber = new Subscriber<String>() {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {

        }

        @Override public void onNext(String s) {
        mTextView.setText(s);
        }
        };
         mObservable.subscribe(mSubscriber);
         */
        /**
         Observable<String> observable = Observable.just("just");

         Action1<String> action1 = new Action1<String>() {
        @Override public void call(String s) {
        // mTextView.setText(s);
        }
        };
         observable.subscribe(action1);
         Observable.just("xxx").subscribe(new Action1<String>() {
        @Override public void call(String s) {
        mTextView.setText(s);
        }
        });

         Observable.just("hahhaha").subscribe(s->mTextView.setText(s+"yyy"));
         */

//        Observable.just("hello world").map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                return s+"-->1";
//            }
//        }).subscribe(s->mTextView.setText(s));

//        Observable.just("hello world").map(s -> s+"-->2").subscribe(s -> mTextView.setText(s));

//        Observable.just("hello world").map(s -> s.hashCode()).map(i -> i.toString()).subscribe(s -> mTextView.setText(s));
//        Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("hello");
//                subscriber.onNext("-->");
//                subscriber.onNext("world");
//                subscriber.onCompleted();
//            }
//        });
//
//        Subscriber<String> mSubscriber = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//        };
//        mObservable.subscribe(mSubscriber);

//        Observable.just(1,2,3,4).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> Log.i("RxJava",""+s));


//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.HOST)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//
//        ApiService apiService = retrofit.create(ApiService.class);

//        Call<TopicListBean> call = apiService.getTopicListBean(58, "android", "test", "com.esvideo", 1, 10, 0);
//        RetroFactory.createApi(this,ApiService.class);
//        Call<HotWordBean> call = apiService.getHotWordBean(58, "android", "test", "com.esvideo");
//        call.enqueue(new Callback<TopicListBean>() {
//            @Override
//            public void onResponse(Call<TopicListBean> call, Response<TopicListBean> response) {
//                TopicListBean topicListBean = response.body();
//                mTextView.setText(topicListBean.topics.get(0).name);
//            }
//
//            @Override
//            public void onFailure(Call<TopicListBean> call, Throwable t) {
//
//            }
//        });
//        Call<HotWordBean> call = RetroFactory.createApi(this, ApiService.class).getHotWordBean(58, "android", "test", "com.esvideo");
//        call.enqueue(new Callback<HotWordBean>() {
//            @Override
//            public void onResponse(Call<HotWordBean> call, Response<HotWordBean> response) {
//                HotWordBean hotWordBean = response.body();
//                mTextView.setText(response.isSuccessful()+"-->"+response.code()+"-->"+response.message()+"-->"+hotWordBean.words);
//            }
//            @Override
//            public void onFailure(Call<HotWordBean> call, Throwable t) {
//                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

        Observable<HotWordBean> observable = RetroFactory.createApi(RxApplication.getInstance(), ApiService.class).getHotWordBean(58, "android", "test", "com.esvideo");
        Subscription subscribe = observable.subscribeOn(Schedulers.io())//Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotWordBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("RxJava", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("RxJava", "Throwable" + e.toString());
                    }

                    @Override
                    public void onNext(HotWordBean hotWordBean) {
                        Log.i("RxJava", "onNext" + hotWordBean.words.toString());
                        mTextView.setText(String.format("-->%s", hotWordBean.words));
                    }
                });


        HashMap<String, String> map = new HashMap<>();
        map.put("version", String.valueOf(58));
        map.put("os", "android");
        map.put("c", "test");
        map.put("pk", "com.esvideo");
        map.put("pn", String.valueOf(1));
        map.put("ps", String.valueOf(10));
        map.put("cversion", String.valueOf(0));

        Observable<TopicListBean> topicListBeanObservable = RetroFactory.createApi(RxApplication.getInstance(), ApiService.class).getTopicListBean(map);
        topicListBeanObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopicListBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("map---->", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("map---->onError-->", e.toString());
                    }

                    @Override
                    public void onNext(TopicListBean topicListBean) {
                        Log.i("map---->", topicListBean.topics.get(0).name);
                        Picasso.with(MainActivity.this).load(topicListBean.topics.get(0).imgUrl).into(iv);
                    }
                });

        ArrayList<String> arrayList = new ArrayList<>();
        String mString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < mString.length(); i++) {
            arrayList.add(String.valueOf(mString.charAt(i)));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        mListView.addHeaderView(headerView);
        mListView.setAdapter(arrayAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


//                if (getScrollY()>0){
//                    float percentage = calculateScrollPercentage(getScrollY());
//                    if (firstVisibleItem > 0) {// 以防万一
//                        int newColor = Color.argb((int) (242 * 0.6), Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(Color.GREEN));//95%的透明
//                        mTitle.setBackgroundColor(newColor);
//                    }else{
//                        int transparentColor = 0x00000000;
//                        mTitle.setBackgroundColor(transparentColor);
//                    }
//                }else {
//                    int transparentColor = 0x00000000;
//                    mTitle.setBackgroundColor(transparentColor);
//                }
                float percentage = calculateScrollPercentage(getScrollY());
                Log.i("kimi", "y-->" + getScrollY() + ",percentage-->" + percentage);
                if (percentage < 1f && percentage > 0.1f) {
                    int newColor = Color.argb((int) (242 * percentage), Color.red(getResources().getColor(R.color.titlebar_bg)), Color.green(getResources().getColor(R.color.titlebar_bg)), Color.blue(getResources().getColor(R.color.titlebar_bg)));//95%的透明
                    mTitle.setBackgroundColor(newColor);
                } else if (percentage >= 1f) {
                    mTitle.setBackgroundColor(getResources().getColor(R.color.titlebar_bg));
                } else {
                    int transparentColor = 0x00000000;
                    mTitle.setBackgroundColor(transparentColor);
                }

            }
        });


//        mListView.setOnScrollListener(new CustomListView.OnScrollListener() {
//            @Override
//            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                float percentage = calculateScrollPercentage(getScrollY());
//                Log.i("kimi","y-->"+t+",percentage-->"+percentage);
//                if (percentage < 1f && percentage > 0.0f) {
//                    int newColor = Color.argb((int) (242 * percentage), Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(Color.GREEN));//95%的透明
//                    mTitle.setBackgroundColor(newColor);
//                } else if (percentage >= 1f) {
//                    mTitle.setBackgroundColor(Color.BLUE);
//                } else {
//                    int transparentColor = 0x00000000;
//                    mTitle.setBackgroundColor(transparentColor);
//                }
//            }
//        });

    }

    private float calculateScrollPercentage(int currentScrollY) {
        float percentage = 0f;
//        if (Build.VERSION.SDK_INT >= 19) {
//            percentage = ((float) currentScrollY) / (mMaxHeight - mStatusBarHeight - mTitleBarHeight);
//        } else {
//            percentage = ((float) currentScrollY) / (mMaxHeight - mTitleBarHeight);
//        }
        percentage = ((float) currentScrollY) / mMaxHeight;
        return percentage;
    }

    private int getScrollY() {
        if (mListView == null) {
            return 0;
        }
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    protected void onStart() {
        Log.i("Life", "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("Life", "onRestart");
        super.onRestart();
        mHandler.getLooper();
    }

    @Override
    protected void onPause() {
        Log.i("Life", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("Life", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            unregisterListener();
        }
    }

    @Override
    protected void onResume() {
        Log.i("Life", "onResume");
        super.onResume();
    }

    public static String httpGet(String httpUrl) {
        BufferedReader input = null;
        StringBuilder sb = null;
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(httpUrl);
            try {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                if (url.getProtocol().toLowerCase().equals("https")) {
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    con = https;
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }
//                con = (HttpURLConnection)url.openConnection();
                input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String s;
                while ((s = input.readLine()) != null) {
                    sb.append(s).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } finally {
            // close buffered
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // disconnecting releases the resources held by a connection so they may be closed or reused
            if (con != null) {
                con.disconnect();
            }
        }
        String str = sb == null ? null : sb.toString();
        Log.i("https--------->", str);
        return str;
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkServerTrusted");
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            //TODO:
//        }
//    };


    private static class StaticHandler extends Handler {
        WeakReference<MainActivity> mActivityWeakReference; //通过弱引用持有Activity对象

        StaticHandler(MainActivity mainActivity) {
            mActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mActivityWeakReference.get();
            if (mainActivity != null) {
                //TODO:
                mainActivity.mTextView.setVisibility(View.VISIBLE);
                mainActivity.registerListener();
            }
        }
    }


    private SensorManager sensorManager;
    private Sensor sensor;
    TimerTask mTimerTask;

    /**
     * 注册监听器
     */
    private void registerListener() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
            sensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    /**
     * 注销监听器 在onDestroy的时候一定要注销，防止产生内存泄漏
     */
    private void unregisterListener() {
        sensorManager.unregisterListener(mListener, sensor);
    }


    private SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


}
