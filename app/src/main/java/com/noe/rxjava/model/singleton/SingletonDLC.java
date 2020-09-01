package com.noe.rxjava.model.singleton;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * DCL式(Double Check Lock)
 * Created by lijie on 2020-03-18.
 */
public class SingletonDLC {

    private volatile static SingletonDLC mInstance;

    private SingletonDLC() {
    }

    public static SingletonDLC getInstance() {
        if (mInstance == null) {
            synchronized (SingletonDLC.class) {
                if (mInstance == null) {
                    mInstance = new SingletonDLC();
                }
            }
        }
        return mInstance;
    }

    /**
     * context 使用applicationContext
     * 如果单例中有使用到view，用弱引用
     */
    private WeakReference<View> myView;

    public void setMyView(View myView) {
        this.myView = new WeakReference<>(myView);
    }
}
