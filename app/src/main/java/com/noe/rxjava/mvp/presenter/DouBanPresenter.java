package com.noe.rxjava.mvp.presenter;

import android.content.Context;

import com.noe.rxjava.bean.DouBanMovieBean;
import com.noe.rxjava.data.DouBanApiService;
import com.noe.rxjava.data.DouBanRetroFactory;
import com.noe.rxjava.mvp.contract.DouBanContract;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lijie24 on 2017/4/21.
 */

public class DouBanPresenter implements DouBanContract.Presenter {
    private final DouBanContract.View mView;

    @Inject
    DouBanPresenter(DouBanContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe(Context context) {
        getDouBanMovieBean(context);
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void getDouBanMovieBean(Context context) {
        Observable<DouBanMovieBean> observable = DouBanRetroFactory.createApi(context, DouBanApiService.class).getDouBanMovieBean();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouBanMovieBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onNext(DouBanMovieBean douBanMovieBean) {
                        mView.setContent(douBanMovieBean);
                    }
                });
    }
}
