package com.noe.rxjava.data;

import com.noe.rxjava.bean.HotWordBean;
import com.noe.rxjava.bean.TopicListBean;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by 58 on 2016/7/15.
 * 访问方法
 */
public interface ApiService {

    @GET("n/api/topic")
    Observable<TopicListBean> getTopicListBean(@QueryMap HashMap<String, String> map);

    @GET("n/api/hotword")
    Observable<HotWordBean> getHotWordBean(@Query("version") int version, @Query("os") String os, @Query("c") String c, @Query("pk") String pk);

    @FormUrlEncoded//表示对POST请求的参数进行编码，这是Retrofit2强制要求的，否则运行会报错
    @POST("user")
    Call<String> getUser(@Field("name") String name, @Field("password") String password);
}
