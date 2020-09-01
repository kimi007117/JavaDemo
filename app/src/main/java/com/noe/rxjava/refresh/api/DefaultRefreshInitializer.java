package com.noe.rxjava.refresh.api;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 默认全局初始化器
 * Created by lijie24 on 2019/8/30.
 */
public interface DefaultRefreshInitializer {
    void initialize(@NonNull Context context, @NonNull RefreshLayout layout);
}
