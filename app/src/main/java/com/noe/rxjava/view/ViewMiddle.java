package com.noe.rxjava.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import timber.log.Timber;

/**
 * Created by lijie24 on 2017/3/9.
 */

public class ViewMiddle extends ViewGroup {


    public ViewMiddle(Context context) {
        super(context);
    }

    public ViewMiddle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewMiddle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Timber.i("Touch", "ViewMiddle view->dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Timber.i("Touch", "ViewMiddle view->onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Timber.i("Touch", "ViewMiddle view->onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }


}
