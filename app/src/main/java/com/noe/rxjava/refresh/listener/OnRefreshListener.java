package com.noe.rxjava.refresh.listener;

import androidx.annotation.NonNull;

import com.noe.rxjava.refresh.api.RefreshLayout;


/**
 * 刷新监听器
 * Created by lijie24 on 2019/8/30.
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
