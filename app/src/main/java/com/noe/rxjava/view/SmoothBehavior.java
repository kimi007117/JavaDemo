package com.noe.rxjava.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppBarLayout.Behavior;

/**
 * Created by lijie on 2018/2/24.
 * Android v26版本AppBarLayout快速滑动导致回弹的解决方案
 * 解决当快速上下滚动，最后回到顶部时，AppBarLayout会出现回弹(bounce)的现象
 * 使用：
 * <com.google.android.material.appbar.AppBarLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:layout_behavior="com.noe.rxjava.view.SmoothBehavior">
 * ......
 * </com.google.android.material.appbar.AppBarLayout>
 */

public class SmoothBehavior extends Behavior {
    private static final int TYPE_FLING = 1;

    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public SmoothBehavior() {
    }

    public SmoothBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        Log.i("TestBehavior", "onInterceptTouchEvent:" + child.getTotalScrollRange());
        shouldBlockNestedScroll = false;
        if (isFlinging) {
            shouldBlockNestedScroll = true;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        //注意看ViewCompat.TYPE_TOUCH
        Log.i("TestBehavior", "onNestedPreScroll:" + child.getTotalScrollRange() + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type);

        //返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
            dxUnconsumed, int dyUnconsumed, int type) {
        Log.i("TestBehavior", "onNestedScroll: target:" + target.getClass() + " ," + child.getTotalScrollRange() + " ,dxConsumed:"
                + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type);
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }
}
