package com.noe.rxjava.image;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by lijie24 on 2017/7/20.
 */

public interface ImageLoaderStrategy {
    void showImage(@NonNull ImageLoaderOptions options);

    void cleanMemory(Context context);

    // 在application的oncreate中初始化
    void init(Context context);
}
