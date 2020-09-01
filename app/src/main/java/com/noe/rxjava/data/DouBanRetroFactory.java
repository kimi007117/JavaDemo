package com.noe.rxjava.data;

import android.content.Context;

import com.noe.rxjava.base.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by 58 on 2016/7/19.
 * 封装Retrofit
 */
public class DouBanRetroFactory {
    private static Retrofit singleton;

    public static <T> T createApi(Context context, Class<T> clazz) {
        if (singleton == null) {
            synchronized (DouBanRetroFactory.class) {
                if (singleton == null) {
                    singleton = new Retrofit.Builder()
                            .baseUrl(Constants.DOUBAN_HOST)
                            .addConverterFactory(JacksonConverterFactory.create())
                            .client(OkHttpFactory.getInstance(context))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return singleton.create(clazz);
    }
}
