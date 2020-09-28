package com.noe.rxjava.view;

import android.content.Context;
import android.os.Build;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by lijie24 on 2016/11/30.
 * 解决vivo手机冲突的问题
 */

public class ScrollableCoordinatorLayout extends CoordinatorLayout {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<View> mViews = new ArrayList();

    public ScrollableCoordinatorLayout(Context context) {
        super(context);
    }

    public ScrollableCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addView(View paramView) {
        mViews.add(paramView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) { // 修复vivo下拉刷新的bug
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() && Build.MODEL.contains("vivo")) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    public void onViewAdded(View paramView) {
        super.onViewAdded(paramView);
        if ((paramView instanceof AppBarLayout)) {

        }
    }

    public void setRefreshView(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }
}
