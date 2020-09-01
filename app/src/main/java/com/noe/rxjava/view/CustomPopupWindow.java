package com.noe.rxjava.view;

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
 * Created by lijie24 on 2017/9/18.
 */

public class CustomPopupWindow extends PopupWindow {
    private Context mContext;

    public CustomPopupWindow(Context context) {
        super(context);
        mContext = context;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View viewContainer = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        setContentView(viewContainer);
    }

    @Override
    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT == 24) { // 7.0单独处理
//            int[] a = new int[2];
//            anchor.getLocationInWindow(a);
//            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, a[1] + anchor.getHeight());
//            return;
//        }
//        if (Build.VERSION.SDK_INT == 25) { // 7.1处理方式
//            Rect rect = new Rect();
//            anchor.getGlobalVisibleRect(rect);
//            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            setHeight(h);
//        }
        if (!isShowing()) {
            super.showAsDropDown(anchor);
            update();
        } else {
            update();
        }
    }
}
