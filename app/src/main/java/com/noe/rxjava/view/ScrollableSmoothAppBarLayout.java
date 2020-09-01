package com.noe.rxjava.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;

/**
 * Created by lijie24 on 2017/6/26.
 * 自定义AppBarLayout
 */

public class ScrollableSmoothAppBarLayout extends SmoothAppBarLayout {
    private ScrollableViewPager.OnTouchAppBarListener mOnTouchAppBarListener;

    public ScrollableSmoothAppBarLayout(Context context) {
        super(context);
    }

    public ScrollableSmoothAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnTouchAppBarListener(ScrollableViewPager.OnTouchAppBarListener onTouchAppBarListener) {
        mOnTouchAppBarListener = onTouchAppBarListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnTouchAppBarListener != null) { // 手指开始触摸
                    mOnTouchAppBarListener.onTouchAppBar(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
