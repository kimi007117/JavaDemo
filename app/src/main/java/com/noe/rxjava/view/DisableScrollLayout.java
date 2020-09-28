package com.noe.rxjava.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by lijie24 on 2017/5/22.
 */

public class DisableScrollLayout extends FrameLayout {
    private int mHeaderHeight;
    private double mDownX;
    private double mDownY;
    private boolean mCanScroll = true;

    public DisableScrollLayout(@NonNull Context context) {
        super(context);
    }

    public DisableScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHeaderViewHeight(int headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    public void setCanScroll(boolean canScroll) {
        this.mCanScroll = canScroll;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = ev.getX();
            mDownY = ev.getY();
        }
        if (ev.getAction() != MotionEvent.ACTION_MOVE || ev.getY() > mHeaderHeight) {
            return super.dispatchTouchEvent(ev);
        }
        double y = ev.getY();
        if (Math.abs(ev.getX() - mDownX) > Math.abs(y - mDownY) || (y > mDownY && !mCanScroll)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
