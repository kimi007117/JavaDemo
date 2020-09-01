package com.noe.rxjava.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.core.base.BaseApplication;
import com.android.core.util.AndroidUtils;
import com.baidu.mapapi.SDKInitializer;
import com.github.moduth.blockcanary.BlockCanary;
import com.noe.rxjava.BuildConfig;
import com.noe.rxjava.data.LocationService;
import com.noe.rxjava.image.ImageLoaderManager;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by 58 on 2016/7/14.
 * 应用初始化
 */
public class RxApplication extends BaseApplication {

    public LocationService locationService;

    private static RxApplication instance;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ImageLoaderManager.getInstance().init(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        BlockCanary.install(this, new AppContext()).start();

        locationService = new LocationService(getApplicationContext());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(instance);
        SDKInitializer.initialize(this);
        initWebView(this);
    }

    public static RxApplication getInstance() {
        return instance;
    }

    public static int getWidth() {
        DisplayMetrics displayMetrics = instance.getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 字体不随系统全局字体大小的变动而变动
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        if (configuration.fontScale != 1.0f) {
            configuration.setToDefaults();
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }

    private void initWebView(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = AndroidUtils.getProcessName(context);
            try {
                //判断不等于默认进程名称
                if (!TextUtils.equals(processName, context.getPackageName())) {
                    WebView.setDataDirectorySuffix(processName);
                }
            } catch (Throwable e){

            }
        }
    }

}
