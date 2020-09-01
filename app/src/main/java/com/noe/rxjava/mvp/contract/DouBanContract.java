package com.noe.rxjava.mvp.contract;

import android.content.Context;

import com.noe.rxjava.bean.DouBanMovieBean;
import com.noe.rxjava.mvp.BasePresenter;
import com.noe.rxjava.mvp.BaseView;

/**
 * Created by lijie24 on 2017/4/21.
 */

public interface DouBanContract {
    interface View extends BaseView<Presenter> {
        void setContent(DouBanMovieBean douBanMovieBean);
    }

    interface Presenter extends BasePresenter {
        void getDouBanMovieBean(Context context);
    }
}
