package com.noe.rxjava.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.noe.rxjava.R;

import java.lang.reflect.Field;


/**
 * 普通类型的toast
 * Created by lijie on 2019/4/1.
 */
public class SimpleToast extends Toast {

    private static final Handler mUIHandler = new Handler(Looper.getMainLooper());

    private static Toast attachToast;



    public SimpleToast(Context context) {
        super(context);

    }

    private static Toast makeText(final Context context, CharSequence text, int duration, int type) {
        if (attachToast == null) {
            attachToast = new SimpleToast(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
            TextView messageText = inflate.findViewById(R.id.text_message);
            setContent(messageText, text, type);
            attachToast.setView(inflate);
        } else {
            View view = attachToast.getView();
            TextView messageText = view.findViewById(R.id.text_message);
            setContent(messageText, text, type);
        }

        if (duration > 2000) {
            attachToast.setDuration(Toast.LENGTH_LONG);
        } else {
            attachToast.setDuration(Toast.LENGTH_SHORT);
        }
        attachToast.setGravity(Gravity.CENTER, 0, 0);

        return attachToast;
    }

    /**
     * 设置内容
     */
    private static void setContent(TextView messageText, CharSequence text, int type) {
        messageText.setText(text);
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 在主线程中执行 runnable 任务
     */
    private static void runOnUiThread(Runnable action) {
        if (isMainThread()) {
            action.run();
        } else {
            new Handler(Looper.getMainLooper()).post(action);
        }
    }

    private static Field sField_TN;
    private static Field sField_TN_Handler;
    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
        }
    }

    private static class SafelyHandlerWrapper extends Handler {

        private Handler impl;

        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
    private void closeAndroidPDialog(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return;
//        try {
//            Class cls = Class.forName("android.app.ActivityThread");
//            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
//            declaredMethod.setAccessible(true);
//            Object activityThread = declaredMethod.invoke(null);
//            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
//            mHiddenApiWarningShown.setAccessible(true);
//            mHiddenApiWarningShown.setBoolean(activityThread, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
