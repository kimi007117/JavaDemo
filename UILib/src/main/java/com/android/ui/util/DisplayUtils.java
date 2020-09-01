package com.android.ui.util;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * Created by lijie on 2020/8/10.
 */
public class DisplayUtils {
    /**
     * 当点击其他View时隐藏软键盘
     *
     * @param activity
     * @param ev
     * @param excludeViews 点击这些View不会触发隐藏软键盘动作
     */
    public static void hideInputWhenTouchOtherView(Activity activity, EditText editText, MotionEvent ev, List<View> excludeViews) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (excludeViews != null && !excludeViews.isEmpty()) {
                for (int i = 0; i < excludeViews.size(); i++) {
                    if (isTouchView(excludeViews.get(i), ev)) {
                        return;
                    }
                }
            }
            View v = activity.getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
    }

    public static boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        int[] leftTop = {0, 0};
//        view.getLocationInWindow(leftTop);
        view.getLocationOnScreen(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        return event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom;
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            return !isTouchView(v, event);
        }
        return false;
    }

    /**
     * 设置EditText编辑状态
     */
    public static void setEditState(EditText editText, boolean isEdit) {
        editText.setFocusable(isEdit);
        editText.setFocusableInTouchMode(isEdit);
        editText.setCursorVisible(isEdit);
        if (isEdit) {
            editText.requestFocus();
        }
    }
}
