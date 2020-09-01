package com.noe.rxjava.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.noe.rxjava.R;

/**
 * Created by lijie on 2020-05-25.
 */
public class CustomPopup2 extends RelativePopupWindow {

    public CustomPopup2(Context context) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_view2, null);
        setContentView(contentView);
        setAlpha(0.5f);
//        contentView.findViewById(R.id.view_background).setOnClickListener(v -> dismiss());
    }

//    @Override
//    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y, boolean fitInScreen) {
//        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            circularReveal(anchor);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void circularReveal(@NonNull final View anchor) {
//        final View contentView = getContentView();
//        contentView.post(new Runnable() {
//            @Override
//            public void run() {
//                final int[] myLocation = new int[2];
//                final int[] anchorLocation = new int[2];
//                contentView.getLocationOnScreen(myLocation);
//                anchor.getLocationOnScreen(anchorLocation);
//                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth()/2;
//                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight()/2;
//
//                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
//                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
//                final float finalRadius = (float) Math.hypot(dx, dy);
//                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
//                animator.setDuration(500);
//                animator.start();
//            }
//        });
//    }
}
