package com.noe.rxjava.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noe.rxjava.R;


/**
 * Created by lijie24 on 2017/6/19.
 * 自定义tablayout
 */

public class PagerTabLayout extends HorizontalScrollView {

    private LinearLayout.LayoutParams mDefaultTabLayoutParams;
    private LinearLayout.LayoutParams mExpandedTabLayoutParams;

    private final PageListener mPageListener = new PageListener();
    public OnPageChangeListener mDelegatePageListener;

    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;

    private int mTabCount; // tab数量
    private int mCurrentPosition = 0; // 当前位置
    private float mCurrentPositionOffset = 0f; // 当前位置偏移量

    private Paint mRectPaint; // 画笔

    private int mIndicatorColor = 0xFF39BC30;// 下划线颜色
    private boolean mShouldExpand = false; // 是否扩展
    private int mScrollOffset = 52;
    private int mIndicatorHeight = 2; // 下划线高度
    private int mIndicatorPadding = 0; // 下划线Padding 有indicatorWidth的时候不需要
    private int mIndicatorWidth = 0; // 下划线宽度 和indicatorPadding选其一
    private int mTabPadding = 6;
    private int mTabTextSize = 16; // tab 字体大写
    private int mTabTextColorSelected = 0xFF333333; // tab 字体选中颜色
    private int mTabTextColorUnselected = 0xFF999999; // tab 字体未选中颜色

    private int mLastScrollX = 0;

    public PagerTabLayout(Context context) {
        this(context, null);
    }

    public PagerTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTabsContainer);

        // get custom attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerTabLayout);
        mTabTextSize = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabTextSize, mTabTextSize);
        mTabTextColorSelected = a.getColor(R.styleable.PagerTabLayout_tabTextColorSelected, mTabTextColorSelected);
        mTabTextColorUnselected = a.getColor(R.styleable.PagerTabLayout_tabTextColorUnSelected, mTabTextColorUnselected);
        mIndicatorColor = a.getColor(R.styleable.PagerTabLayout_tabUnderLineColor, mIndicatorColor);
        mIndicatorHeight = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabUnderLineHeight, mIndicatorHeight);
        mIndicatorPadding = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabUnderLinePadding, mIndicatorPadding);
        mIndicatorWidth = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabUnderLineWidth, mIndicatorWidth);
        mTabPadding = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabPaddingLeftRight, mTabPadding);
        mShouldExpand = a.getBoolean(R.styleable.PagerTabLayout_tabShouldExpand, mShouldExpand);
        mScrollOffset = a.getDimensionPixelSize(R.styleable.PagerTabLayout_tabScrollOffset, mScrollOffset);
        a.recycle();

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Style.FILL);

        mDefaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mExpandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    public void setViewPager(ViewPager pager) {
        this.mViewPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("PagerTabLayout:ViewPager does not have adapter instance.");
        }
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
            if (mViewPager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
            }
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
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

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });

        tab.setPadding(mTabPadding, 0, mTabPadding, 0);
        mTabsContainer.addView(tab, position, mShouldExpand ? mExpandedTabLayoutParams : mDefaultTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
                if (i == 0) {
                    tab.setTextColor(mTabTextColorSelected);
                    tab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    tab.setTextColor(mTabTextColorUnselected);
                    tab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return;
        }
        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= mScrollOffset;
        }
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount == 0) {
            return;
        }
        final int height = getHeight();
        // draw indicator line
        mRectPaint.setColor(mIndicatorColor);
        // default: line below current tab
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        //如果设置底部线宽度，则将其下划线化成固定宽度
        float differ = 0;
        if (mIndicatorWidth > 0) {
            differ = (lineRight - lineLeft - mIndicatorWidth) / 2;
            lineLeft = lineLeft + differ;
            lineRight = lineRight - differ;
        }
        if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
            View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
            final float nextTabLeft = nextTab.getLeft() + differ;
            final float nextTabRight = nextTab.getRight() - differ;
            lineLeft = (mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft);
            lineRight = (mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight);
        }
        if (mIndicatorWidth > 0) { // 如果设置了下划线宽度 mIndicatorPadding就没有什么意义了
            canvas.drawRect(lineLeft, height - mIndicatorHeight, lineRight, height, mRectPaint);
        } else {
            canvas.drawRect(lineLeft + mIndicatorPadding, height - mIndicatorHeight, lineRight - mIndicatorPadding, height, mRectPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));
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
                    tabText.setTextColor(mTabTextColorSelected);
                    tabText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    tabText.setTextColor(mTabTextColorUnselected);
                    tabText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    interface IconTabProvider {
        int getPageIconResId(int position);
    }
}

