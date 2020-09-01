package com.noe.rxjava.window;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.noe.rxjava.R;
import com.noe.rxjava.adapter.GuideAdapter;

import java.util.List;

/**
 * viewpager自动滚动
 * Created by lijie on 2019-07-20.
 */
public class ViewPagerController {

    private Context mContext;
    private GuideAdapter mGuideAdapter;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;

    private Handler mHandler = new Handler();
    private long mSwitchTime = 3000L;
    private boolean isLooping = false;

    public ViewPagerController(Context context, ViewPager viewPager, LinearLayout indicatorLayout) {
        this.mContext = context;
        this.mViewPager = viewPager;
        this.mIndicatorLayout = indicatorLayout;
        mViewPager.setOnTouchListener(mOnTouchListener);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    public void setGuideAdapter(List<Integer> list) {
        if (list == null) {
            return;
        }

        if (mGuideAdapter == null) {
            mGuideAdapter = new GuideAdapter(mContext);
        }
        initIndicatorLayout(list);

        mGuideAdapter.setData(list);
        mViewPager.setAdapter(mGuideAdapter);
        mViewPager.setOffscreenPageLimit(4);

        selectIndicator(0);
        if (list.size() <= 1) {
            mIndicatorLayout.setVisibility(View.GONE);
            stopLoop();
        } else {
            mIndicatorLayout.setVisibility(View.VISIBLE);
            restartLoop();
            startLoop();
        }
    }

    /**
     * 初始化下面的点
     */
    private void initIndicatorLayout(List<Integer> list) {
        if (null == mIndicatorLayout) {
            return;
        }
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setPadding(0, 0, 15, 0);
            mIndicatorLayout.addView(imageView, i);
        }
    }

    /**
     * 设置当前指示点
     *
     * @param position 当前位置
     */
    private void selectIndicator(int position) {
        if (null == mIndicatorLayout || mIndicatorLayout.getChildCount() < 1) {
            return;
        }
        ImageView child;
        for (int i = 0; i < mIndicatorLayout.getChildCount(); i++) {
            child = (ImageView) mIndicatorLayout.getChildAt(i);
            if (position == i) {
                child.setImageResource(R.drawable.shape_guide_dot_selected);
            } else {
                child.setImageResource(R.drawable.shape_guide_dot_default);
            }
        }
    }

    /**
     * viewPager滑动监听
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            selectIndicator(i % mGuideAdapter.getRealCount());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private View.OnTouchListener mOnTouchListener = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            stopLoop();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            startLoop();
        }
        return false;
    };

    //========================= loop ========================
    private Runnable mRunnable = this::switchItem;

    private void switchItem() {
        mHandler.removeCallbacks(mRunnable);
        if (mViewPager != null && mGuideAdapter.getRealCount() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
        mHandler.postDelayed(mRunnable, mSwitchTime);
    }

    public void restartLoop() {
        if (mViewPager != null &&  mGuideAdapter.getRealCount() > 0) {
            mViewPager.setCurrentItem(0);
        }
    }

    public void startLoop() {
        if (mGuideAdapter.getRealCount() < 2) {
            setLooping(false);
            mHandler.removeCallbacks(mRunnable);
            return;
        }
        setLooping(true);
        mHandler.postDelayed(mRunnable, mSwitchTime);
    }

    public void stopLoop() {
        setLooping(false);
        mHandler.removeCallbacks(mRunnable);
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }
//========================= loop ========================
}
