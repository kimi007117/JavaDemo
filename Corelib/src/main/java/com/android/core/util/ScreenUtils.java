package com.android.core.util;


import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lijie24 on 2017/3/3.
 */

public class ScreenUtils {
    /**
     * 解决不能layout不能预览的问题
     * 根据手机的分辨率从 dip 的单位 转成为 pixel(像素)
     */
    public static int dipToPixel(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 pixel(像素) 的单位 转成为 dip
     */
    public static int pixelToDip(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int mStatusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return mStatusBarHeight;
    }
}
