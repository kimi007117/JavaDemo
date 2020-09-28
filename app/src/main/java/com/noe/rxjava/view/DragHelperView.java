package com.noe.rxjava.view;

import android.content.Context;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by 58 on 2016/7/29.
 */
public class DragHelperView extends LinearLayout {

    private ViewDragHelper mDragHelper;


    public DragHelperView(Context context) {
        super(context);
    }

    public DragHelperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDragHelper();
    }

    private void initDragHelper() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //取得左边界的坐标
                final int leftBound = getPaddingLeft();
                //取得右边界的坐标
                final int rightBound = getWidth() - child.getWidth() - leftBound;
                //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
                //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
                //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;
                return Math.min(Math.max(top, topBound), bottomBound);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
