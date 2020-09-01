package com.noe.rxjava.dagger;

import android.content.Context;
import android.util.Log;

/**
 * Created by lijie24 on 2017/1/16.
 */

public class Person {
    private Context mContext;

    public Person(Context context) {
        this.mContext = context;
        Log.i("dagger", "person create!!!" + mContext);
    }

    public String getText() {
        return "dagger";
    }
}
