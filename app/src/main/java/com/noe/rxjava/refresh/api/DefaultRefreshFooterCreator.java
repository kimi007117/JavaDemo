package com.noe.rxjava.refresh.api;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * 默认Footer创建器
 * Created by lijie24 on 2019/8/30.
 */
public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
