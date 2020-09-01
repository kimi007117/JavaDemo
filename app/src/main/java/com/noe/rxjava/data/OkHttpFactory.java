package com.noe.rxjava.data;

import android.content.Context;

import com.noe.rxjava.base.Config;
import com.android.core.util.NetworkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 58 on 2016/7/19.
 * 封装OkHttpClient
 */
public class OkHttpFactory {
    private static OkHttpClient singleton;

    public static OkHttpClient getInstance(Context context) {
        if (singleton == null) {
            synchronized (OkHttpFactory.class) {
                if (singleton == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    File cacheFile = new File(context.getExternalCacheDir(), Config.RESPONSE_CACHE);
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
                    Interceptor cacheInterceptor = chain -> {
                        Request request = chain.request();
                        if (!NetworkUtils.isNetworkAvailable(context)) {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        if (NetworkUtils.isNetworkAvailable(context)) {
                            int maxAge = 0;
                            // 有网络时 设置缓存超时时间0个小时
                            response.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();
                        } else {
                            // 无网络时，设置超时为4周
                            int maxStale = 60 * 60 * 24 * 28;
                            response.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return response;
                    };
                    builder.cache(cache).addInterceptor(cacheInterceptor);
                    //设置超时
                    builder.connectTimeout(15, TimeUnit.SECONDS);
                    builder.readTimeout(20, TimeUnit.SECONDS);
                    builder.writeTimeout(20, TimeUnit.SECONDS);
                    //失败重连
                    builder.retryOnConnectionFailure(true);
                    singleton = builder.build();
                }
            }
        }
        return singleton;
    }
}
