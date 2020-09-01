package com.noe.rxjava.base;

import android.content.Context;

import com.noe.rxjava.R;

/**
 * Created by lijie24 on 2017/11/30.
 */

public class AppManager {
    private static AppManager INSTANCE;

    private Context mContext;

    private AppManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static AppManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppManager(context);
        }
        return INSTANCE;
    }

    private int getColor() {
        return mContext.getResources().getColor(R.color.amber_500);
    }

}
