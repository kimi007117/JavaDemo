package com.noe.rxjava.refresh.api;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * 默认Header创建器
 * Created by lijie24 on 2019/8/30.
 */
public interface DefaultRefreshHeaderCreator {
    @NonNull
    RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout);
}
