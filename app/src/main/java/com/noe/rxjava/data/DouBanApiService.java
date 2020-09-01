package com.noe.rxjava.data;

import com.noe.rxjava.bean.DouBanMovieBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lijie24 on 2017/4/20.
 */

public interface DouBanApiService {
    @GET("/v2/movie/in_theaters")
    Observable<DouBanMovieBean> getDouBanMovieBean();
}
