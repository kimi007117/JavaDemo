package com.noe.rxjava.refresh.listener;

import android.support.annotation.NonNull;

import com.noe.rxjava.refresh.api.RefreshLayout;


/**
 * 加载更多监听器
 * Created by lijie24 on 2019/8/30.
 */
public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
