package com.noe.rxjava.refresh.listener;

/**
 * Created by lijie24 on 2019/8/30.
 */
public interface CoordinatorLayoutListener {
    void onCoordinatorUpdate(boolean enableRefresh, boolean enableLoadMore);
}