package com.noe.rxjava.popup;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.widget.PopupWindowCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.android.core.util.ScreenUtils;

/**
 * Created by lijie on 2020-05-25.
 */
public class RelativePopupWindow extends PopupWindow {

    private float mAlpha = 1f; //背景灰度  0-1  1表示全透明

    private Context mContext;

    public RelativePopupWindow(Context context) {
        this(context, null, 0);
    }

    public RelativePopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * Show at relative position to anchor View.
     *
     * @param anchor   Anchor View
     * @param vertPos  Vertical Position Flag
     * @param horizPos Horizontal Position Flag
     */
    public void showOnAnchor(
            @NonNull View anchor,
            @VerticalPosition int vertPos,
            @HorizontalPosition int horizPos
    ) {
        showOnAnchor(anchor, vertPos, horizPos, 0, 0);
    }

    /**
     * Show at relative position to anchor View.
     *
     * @param anchor      Anchor View
     * @param vertPos     Vertical Position Flag
     * @param horizPos    Horizontal Position Flag
     * @param fitInScreen Automatically fit in screen or not
     */
    public void showOnAnchor(
            @NonNull View anchor,
            @VerticalPosition int vertPos,
            @HorizontalPosition int horizPos,
            boolean fitInScreen
    ) {
        showOnAnchor(anchor, vertPos, horizPos, 0, 0, fitInScreen);
    }

    /**
     * Show at relative position to anchor View with translation.
     *
     * @param anchor   Anchor View
     * @param vertPos  Vertical Position Flag
     * @param horizPos Horizontal Position Flag
     * @param x        Translation X
     * @param y        Translation Y
     */
    public void showOnAnchor(
            @NonNull View anchor,
            @VerticalPosition int vertPos,
            @HorizontalPosition int horizPos,
            int x,
            int y
    ) {
        showOnAnchor(anchor, vertPos, horizPos, x, y, true);
    }

    /**
     * Show at relative position to anchor View with translation.
     *
     * @param anchor      Anchor View
     * @param vertPos     Vertical Position Flag
     * @param horizPos    Horizontal Position Flag
     * @param x           Translation X
     * @param y           Translation Y
     * @param fitInScreen Automatically fit in screen or not
     */
    public void showOnAnchor(
            @NonNull View anchor,
            @VerticalPosition int vertPos,
            @HorizontalPosition int horizPos,
            int x,
            int y,
            boolean fitInScreen
    ) {
        setClippingEnabled(fitInScreen);
        showBackgroundAnimator();
        final View contentView = getContentView();
        final Rect windowRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(windowRect);
        final int windowW = windowRect.width();
        final int windowH = windowRect.height();
        contentView.measure(
                makeDropDownMeasureSpec(getWidth(), windowW),
                makeDropDownMeasureSpec(getHeight(), windowH)
        );
        final int measuredW = contentView.getMeasuredWidth();
        final int measuredH = contentView.getMeasuredHeight();
        final int[] anchorLocation = new int[2];
        anchor.getLocationInWindow(anchorLocation);
        final int anchorBottom = anchorLocation[1] + anchor.getHeight();
        if (!fitInScreen) {
            x += anchorLocation[0];
            y += anchorBottom;
        }
        switch (vertPos) {
            case VerticalPosition.ABOVE:
                y -= measuredH + anchor.getHeight();
                break;
            case VerticalPosition.ALIGN_BOTTOM:
                y -= measuredH;
                break;
            case VerticalPosition.CENTER:
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case VerticalPosition.ALIGN_TOP:
                y -= anchor.getHeight();
                break;
            case VerticalPosition.BELOW:
                // Default position.
                break;
        }
        switch (horizPos) {
            case HorizontalPosition.LEFT:
                x -= measuredW;
                break;
            case HorizontalPosition.ALIGN_RIGHT:
                x -= measuredW - anchor.getWidth();
                break;
            case HorizontalPosition.CENTER:
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case HorizontalPosition.ALIGN_LEFT:
                // Default position.
                break;
            case HorizontalPosition.RIGHT:
                x += anchor.getWidth();
                break;
        }
        if (fitInScreen) {
            if (y + anchorBottom < 0) {
                y = -anchorBottom;
            } else if (y + anchorBottom + measuredH > windowH) {
                y = windowH - anchorBottom - measuredH;
            }
            PopupWindowCompat.showAsDropDown(this, anchor, x, y, Gravity.NO_GRAVITY);
        } else {
            showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        }
    }

    private static int makeDropDownMeasureSpec(int measureSpec, int maxSize) {
        return View.MeasureSpec.makeMeasureSpec(
                getDropDownMeasureSpecSize(measureSpec, maxSize),
                getDropDownMeasureSpecMode(measureSpec)
        );
    }

    private static int getDropDownMeasureSpecSize(int measureSpec, int maxSize) {
        switch (measureSpec) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                return maxSize;
            default:
                return View.MeasureSpec.getSize(measureSpec);
        }
    }

    private static int getDropDownMeasureSpecMode(int measureSpec) {
        switch (measureSpec) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                return View.MeasureSpec.UNSPECIFIED;
            default:
                return View.MeasureSpec.EXACTLY;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismissBackgroundAnimator();
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    private void showBackgroundAnimator() {
        if (mAlpha >= 1f) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        animator.start();
    }

    /**
     * 窗口隐藏，窗口背景透明度渐变动画
     */
    private void dismissBackgroundAnimator() {
        if (mAlpha >= 1f) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mAlpha, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        animator.start();
    }

    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof Activity) {
            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }
}
