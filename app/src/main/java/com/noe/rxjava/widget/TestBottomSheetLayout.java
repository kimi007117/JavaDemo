package com.noe.rxjava.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.noe.rxjava.R;
import com.android.core.util.ScreenUtils;

/**
 * Created by lijie on 2020/3/23.
 */
public class TestBottomSheetLayout extends FrameLayout {

    private BottomSheetBehavior<View> mBottomSheetBehavior;


    public TestBottomSheetLayout(Context context) {
        this(context, null);
    }

    public TestBottomSheetLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestBottomSheetLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.layout_bottom_sheet_view, this);
        setPadding(0, (int) (ScreenUtils.getScreenHeight(context) * (1 - 0.9)), 0, 0);
        mBottomSheetBehavior = BottomSheetBehavior.from(inflate.findViewById(R.id.bottom_sheet_layout));
        mBottomSheetBehavior.setPeekHeight(ScreenUtils.dipToPixel(context,500));
        mBottomSheetBehavior.setHideable(true);
//        mBottomSheetBehavior.setSkipCollapsed(true);
        hide();
    }

    public final void hide() {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public final void show() {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}
