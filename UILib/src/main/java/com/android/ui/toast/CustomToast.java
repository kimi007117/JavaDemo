package com.android.ui.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.widget.Toast;

/**
 * Created by lijie on 2019/4/4.
 */
public class CustomToast implements IMToast {

    private Toast actual;

    /**
     * 默认时长2s
     */
    public static final int DEFAULT_DURATION = 2000;

    /**
     * 调用默认时长2s的toast
     */
    public static CustomToast makeText(Context context, SpannableString text) {
        return makeText(context, text, DEFAULT_DURATION, 0);
    }

    /**
     * 调用默认时长2s的toast
     */
    public static CustomToast makeText(Context context, CharSequence text, int type) {
        return makeText(context, text, DEFAULT_DURATION, type);
    }

    public static CustomToast makeText(final Context context, CharSequence text, int duration, int type) {
        CustomToast result = new CustomToast();

        if (context == null) {
            return result;
        }

        result.actual = SimpleToast.makeText(context.getApplicationContext(), text, duration, type);
        return result;
    }


    public void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (actual != null) {
                    actual.show();
                }
            }
        });

    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 在主线程中执行 runnable 任务
     */
    private static void runOnUiThread(Runnable action) {
        if (isMainThread()) {
            new Handler(Looper.getMainLooper()).post(action);
        } else {
            action.run();
        }
    }

}
