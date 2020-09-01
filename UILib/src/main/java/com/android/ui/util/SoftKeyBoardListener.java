package com.android.ui.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 监听键盘变化
 * Created by lijie on 2020/8/10.
 */
public class SoftKeyBoardListener {

    private View rootView;//activity的根视图
    private int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangedListener mOnSoftKeyBoardChangedListener;

    public SoftKeyBoardListener(Activity activity) {
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //获取当前根视图在屏幕上显示的大小
            Rect r = new Rect();
            if (rootView == null) {
                return;
            }
            rootView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return;
            }

            //根视图显示高度变小超过300，可以看作软键盘显示了，该数值可根据需要自行调整
            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (mOnSoftKeyBoardChangedListener != null) {
                    mOnSoftKeyBoardChangedListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度变大超过300，可以看作软键盘隐藏了，该数值可根据需要自行调整
            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (mOnSoftKeyBoardChangedListener != null) {
                    mOnSoftKeyBoardChangedListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                }
                rootViewVisibleHeight = visibleHeight;
            }

        }
    };

    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangedListener onSoftKeyBoardChangedListener) {
        this.mOnSoftKeyBoardChangedListener = onSoftKeyBoardChangedListener;
    }

    public interface OnSoftKeyBoardChangedListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public void destroy() {
        if (rootView != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }
}
