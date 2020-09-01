package com.noe.rxjava;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.fragment.RecyclerPageFragment;
import com.noe.rxjava.util.ArouterUtils;
import com.android.core.util.ScreenUtils;
import com.noe.rxjava.view.PagerBlockTabLayout;
import com.noe.rxjava.view.ScrollableSmoothAppBarLayout;
import com.noe.rxjava.view.ScrollableViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.ObservablePagerAdapter;

/**
 * Created by 58 on 2016/8/5.
 */
@Route(path = ArouterUtils.ACTIVITY_BLOCKTAB)
public class BlockTabActivity extends AppCompatActivity {

    private ScrollableViewPager mViewPager;
    private PagerBlockTabLayout mTabLayout;
    SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    private Bundle savedInstanceState;
    private ScrollableSmoothAppBarLayout mSmoothAppBarLayout;
    private boolean isAttached;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        mContext = this;
        setContentView(R.layout.activity_block_tab);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ScrollableViewPager) findViewById(R.id.view_pager_recycler);
        mTabLayout =  findViewById(R.id.bar_tab);
        mSmoothAppBarLayout = (ScrollableSmoothAppBarLayout) findViewById(R.id.app_bar);
        mViewPager.setAppBarLayout(mSmoothAppBarLayout);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isAttached) {
            SparseArray<String> s;
            int height = mSmoothAppBarLayout.getHeight();
            simpleFragmentPagerAdapter.addFragment("工友圈", RecyclerPageFragment.newInstance(1, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            simpleFragmentPagerAdapter.addFragment("匿名八卦", RecyclerPageFragment.newInstance(2, height));
            mViewPager.setAdapter(simpleFragmentPagerAdapter);
            mTabLayout.setViewPager(mViewPager);
            mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            isAttached = true;
//            setUpIndicatorWidth();
        }
    }

    private void initData() {
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        simpleFragmentPagerAdapter.onRestoreInstanceState(savedInstanceState);
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter implements ObservablePagerAdapter {

        private Context context;
        private final SparseArray<String> mTags = new SparseArray<>();
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<CharSequence> mTitles = new ArrayList<>();
        private FragmentManager mFragmentManager;

        private String makeTagName(int position) {
            return SimpleFragmentPagerAdapter.class.getName() + ":" + position;
        }

        private Bundle mSavedInstanceState = new Bundle();

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            String tagName = mSavedInstanceState.getString(makeTagName(position));
            if (!TextUtils.isEmpty(tagName)) {
                Fragment fragment = mFragmentManager.findFragmentByTag(tagName);
                return fragment != null ? fragment : mFragments.get(position);
            }
            return mFragments.get(position);
        }

        @Override
        public ObservableFragment getObservableFragment(int position) {
            if (getItem(position) instanceof ObservableFragment) {
                return (ObservableFragment) getItem(position);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            mTags.put(position, ((Fragment) object).getTag());
            return object;
        }

        public void addFragment(CharSequence title, Fragment fragment) {
            mTitles.add(title);
            mFragments.add(fragment);
        }

        public void onRestoreInstanceState(Bundle savedInstanceState) {
            mSavedInstanceState = savedInstanceState != null ? savedInstanceState : new Bundle();
        }

        public void onSaveInstanceState(Bundle outState) {
            for (int i = 0; i < mTags.size(); i++) {
                outState.putString(makeTagName(mTags.keyAt(i)), mTags.valueAt(i));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        simpleFragmentPagerAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 通过反射修改TabLayout Indicator的宽度（仅在Android 4.2及以上生效）
     */
    private void setUpIndicatorWidth() {
        Class<?> tabLayoutClass = mTabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(mTabLayout);
            }
            if (layout == null) {
                return;
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(ScreenUtils.dipToPixel(mContext, 36));
                    params.setMarginEnd(ScreenUtils.dipToPixel(mContext, 36));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            Log.i("STATE-->", "verticalOffset-->" + verticalOffset);
            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

}
