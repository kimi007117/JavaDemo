package com.noe.rxjava.mvp;

import android.content.Context;

/**
 * Created by lijie24 on 2017/4/21.
 */

public interface BasePresenter {
    void subscribe(Context context);

    void unSubscribe();
}
