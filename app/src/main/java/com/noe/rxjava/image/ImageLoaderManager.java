package com.noe.rxjava.image;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

/**
 * Created by lijie24 on 2017/7/20.
 */

public class ImageLoaderManager implements ImageLoaderStrategy {
    private static final ImageLoaderManager INSTANCE = new ImageLoaderManager();
    private ImageLoaderStrategy loaderStrategy;

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return INSTANCE;
    }

    public void setImageLoaderStrategy(ImageLoaderStrategy strategy) {
        loaderStrategy = strategy;
    }

    /*
    *   可创建默认的Options设置，假如不需要使用ImageView ，
    *    请自行new一个Imageview传入即可
    *  内部只需要获取Context
    */
    public static ImageLoaderOptions getDefaultOptions(@NonNull View container, @NonNull String url) {
        return new ImageLoaderOptions.Builder(container, url).isCrossFade(true).build();
    }


    @Override
    public void showImage(@NonNull ImageLoaderOptions options) {
        if (loaderStrategy != null) {
            loaderStrategy.showImage(options);
        }
    }

    @Override
    public void cleanMemory(Context context) {
        if (loaderStrategy != null) {
            loaderStrategy.cleanMemory(context);
        }
    }

    @Override
    public void init(Context context) {
        loaderStrategy = new GlideImageLoader();
        loaderStrategy.init(context);
    }
}
