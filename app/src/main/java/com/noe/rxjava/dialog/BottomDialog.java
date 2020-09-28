package com.noe.rxjava.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.core.util.ScreenUtils;
import com.android.ui.util.SoftKeyBoardListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseBottomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 2020/8/4.
 */
public class BottomDialog extends BaseBottomDialog {

    private Context mContext;
    private EditText mEditText;
    private Button mBtnEdit;
    private SoftKeyBoardListener mSoftKeyBoardListener;

    public BottomDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom);
        Window window = getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getDialogHeight());
        window.setGravity(Gravity.BOTTOM);
        cancelDrag();
        initView();
    }

    private void initView() {
        mEditText = findViewById(R.id.et_title);
        mBtnEdit = findViewById(R.id.btn_edit);
        setEditState(mEditText, false);
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditState(mEditText, true);
                mBtnEdit.setVisibility(View.GONE);
                //强制调用键盘
                InputMethodManager inputMethodManager =
                        (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mSoftKeyBoardListener = new SoftKeyBoardListener((Activity) mContext);

        mSoftKeyBoardListener.setOnSoftKeyBoardChangeListener(new SoftKeyBoardListener.OnSoftKeyBoardChangedListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(mContext, "keyBoardShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(mContext, "keyBoardHide", Toast.LENGTH_SHORT).show();
                setEditState(mEditText, false);
                mBtnEdit.setVisibility(View.VISIBLE);
            }
        });

//        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                Toast.makeText(mContext, "keyBoardShow", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//
//            }
//        });
    }

    //排除的View
    private List<View> getExcludeTouchHideInputViews() {
        List<View> views = new ArrayList<>();
        views.add(mBtnEdit);
        return views;
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);


    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置EditText编辑状态
     */
    private void setEditState(EditText editText, boolean isEdit) {
        editText.setFocusable(isEdit);
        editText.setFocusableInTouchMode(isEdit);
        editText.setCursorVisible(isEdit);
        if (isEdit) {
            editText.requestFocus();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSoftKeyBoardListener != null) {
            mSoftKeyBoardListener.destroy();
        }
    }

    /**
     * 取消拖拽
     */
    private void cancelDrag() {
        View root = getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        assert root != null;
        root.setBackgroundColor(Color.TRANSPARENT);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
        behavior.setHideable(false);
        behavior.setPeekHeight(getDialogHeight());
    }

    /**
     * 设置dialog高度，将PeekHeight也设置相同的高度
     * 这样就可以禁止dialog的滑动和折叠了
     */
    private int getDialogHeight() {
        return (int) (ScreenUtils.getScreenHeight(mContext) * 0.6);
    }
}
