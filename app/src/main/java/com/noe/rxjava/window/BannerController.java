package com.noe.rxjava.window;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.noe.rxjava.R;
import com.noe.rxjava.adapter.BannerAdapter;

import java.util.List;

/**
 * Created by lijie on 2019-07-20.
 */
public class BannerController {

    private Context mContext;
    private BannerAdapter mBannerAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mIndicatorLayout;

    public BannerController(@NonNull Context context, @NonNull RecyclerView recyclerView, @NonNull LinearLayout indicatorLayout){
        mContext = context;
        mRecyclerView = recyclerView;
        mIndicatorLayout = indicatorLayout;
        mRecyclerView.setOnTouchListener(mOnTouchListener);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    public void setAdapter(List<Integer> list){
        if (list==null){
            return;
        }
        if (null == mBannerAdapter) {
            mBannerAdapter = new BannerAdapter(mContext);
        }
        initIndicatorLayout(list);

        mBannerAdapter.setData(list);

        final SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setOnFlingListener(null);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mBannerAdapter);

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

    private RecyclerView.OnScrollListener mOnScrollListener= new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //DLog.d(TAG, "onScrollStateChanged:" + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int mIndex = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() % mBannerAdapter.getRealCount();
                selectIndicator(mIndex);
            }
        }
    };

    private View.OnTouchListener mOnTouchListener = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stopLoop();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            startLoop();
        }
        return false;
    };

//========================= loop ========================

    private Handler mHandler = new Handler();
    private long mSwitchTime = 3000L;
    private boolean isLooping = false;

    public void setSwitchTime(long switchTime) {
        mSwitchTime = switchTime;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            switchItem();
        }
    };

    private void switchItem() {
        mHandler.removeCallbacks(mRunnable);
        if (mRecyclerView != null && mBannerAdapter.getRealCount() > 0) {
            mRecyclerView.smoothScrollToPosition(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1);
        }
        mHandler.postDelayed(mRunnable, mSwitchTime);
    }

    public void restartLoop() {
        if (mRecyclerView != null && mBannerAdapter.getRealCount() > 0) {
            mRecyclerView.smoothScrollToPosition(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1);
        }
    }

    public void startLoop() {
        if (mBannerAdapter.getRealCount() < 2) {
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
