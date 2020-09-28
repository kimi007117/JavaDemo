package com.noe.rxjava.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noe.rxjava.R;


/**
 * Created by lijie24 on 2017/6/19.
 * 自定义tablayout
 */

public class PagerBlockTabLayout extends HorizontalScrollView {

    private LinearLayout.LayoutParams mDefaultTabLayoutParams;

    private final PageListener mPageListener = new PageListener();
    public OnPageChangeListener mDelegatePageListener;

    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;

    private int mTabCount; // tab数量
    private int mCurrentPosition = 0; // 当前位置

    private int mTabPadding;
    private int mTabMargin;
    private int mTabTextSize = 14; // tab 字体大写
    private int mTabTextColorSelected = 0xFFFFFFFF; // tab 字体选中颜色
    private int mTabTextColorUnselected = 0xFF9FA4B0; // tab 字体未选中颜色

    private int mLastScrollX = 0;

    public PagerBlockTabLayout(Context context) {
        this(context, null);
    }

    public PagerBlockTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerBlockTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTabsContainer);

        mTabPadding = dp2px(15);
        mTabMargin = dp2px(5);

        mDefaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    public void setViewPager(ViewPager pager) {
        this.mViewPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("PagerTabLayout:ViewPager does not have adapter instance.");
        }
        pager.removeOnPageChangeListener(mPageListener);
        pager.addOnPageChangeListener(mPageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        mTabCount = mViewPager.getAdapter().getCount();
        for (int i = 0; i < mTabCount; i++) {
            addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mCurrentPosition = mViewPager.getCurrentItem();
                scrollToChild(mCurrentPosition, 0);
            }
        });
    }

    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(v -> mViewPager.setCurrentItem(position));
        mTabsContainer.addView(tab, position, mDefaultTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2px(mTabTextSize));
                tab.setPadding(mTabPadding, 0, mTabPadding, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                if (i < mTabCount - 1) {
                    params.setMargins(0, 0, mTabMargin * 2, 0);
                }
                tab.setLayoutParams(params);
                if (i == 0) {
                    tab.setBackground(getResources().getDrawable(R.drawable.bg_tab_selected_shape));
                    tab.setTextColor(mTabTextColorSelected);
                } else {
                    tab.setBackground(getResources().getDrawable(R.drawable.bg_tab_unselected_shape));
                    tab.setTextColor(mTabTextColorUnselected);
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount <= 0) {
            return;
        }
        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            scrollToChild(position, (int) (positionOffset * (mTabsContainer.getChildAt(position).getWidth() + mTabMargin * 2)));
            invalidate();
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
            for (int i = 0; i < mTabsContainer.getChildCount(); i++) {
                TextView tabText = (TextView) mTabsContainer.getChildAt(i);
                if (i == position) {
                    tabText.setBackground(getResources().getDrawable(R.drawable.bg_tab_selected_shape));
                    tabText.setTextColor(mTabTextColorSelected);
                } else {
                    tabText.setBackground(getResources().getDrawable(R.drawable.bg_tab_unselected_shape));
                    tabText.setTextColor(mTabTextColorUnselected);
                }
            }
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<PagerBlockTabLayout.SavedState>() {
            @Override
            public PagerBlockTabLayout.SavedState createFromParcel(Parcel in) {
                return new PagerBlockTabLayout.SavedState(in);
            }

            @Override
            public PagerBlockTabLayout.SavedState[] newArray(int size) {
                return new PagerBlockTabLayout.SavedState[size];
            }
        };
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}

