package com.noe.rxjava.view;


import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lijie24 on 2017/6/27.
 * 自定义是否可以左右滑动的viewpager
 */

public class ScrollableViewPager extends ViewPager {
    private int mDownX;
    private int mDownY;
    private boolean mTouched; // 是否正在触摸appbar
    private boolean mExpanded = true; // appbar是否是在展开状态

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAppBarLayout(ScrollableSmoothAppBarLayout scrollableSmoothAppBarLayout) {
        if (scrollableSmoothAppBarLayout != null) {
            scrollableSmoothAppBarLayout.setOnTouchAppBarListener(mOnTouchAppBarListener);
            scrollableSmoothAppBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getRawX();
                mDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                int moveY = (int) ev.getRawY();
                if (mTouched) { // 正在触摸appbar
                    if (Math.abs(moveX - mDownX) > Math.abs(moveY - mDownY)) { // 左右不让滑动
                        return true;
                    }
                    if (mExpanded && Math.abs(moveY - mDownY) > Math.abs(moveX - mDownX) && (moveY - mDownY) > 0) { // 如果是在展开状态并且往下滑时
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mOnTouchAppBarListener != null) { // 手指抬起
                    mOnTouchAppBarListener.onTouchAppBar(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) { // 监听appbar展开状态
            mExpanded = verticalOffset == 0;
        }
    };

    private OnTouchAppBarListener mOnTouchAppBarListener = new OnTouchAppBarListener() {
        @Override
        public void onTouchAppBar(boolean touched) {
            mTouched = touched;
        }
    };

    interface OnTouchAppBarListener {
        void onTouchAppBar(boolean touched);
    }
}
