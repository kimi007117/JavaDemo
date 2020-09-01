package com.noe.rxjava.mvp;

/**
 * Created by lijie24 on 2017/4/21.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
