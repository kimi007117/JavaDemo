package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.ui.widget.PullDownHolder;
import com.android.ui.widget.RefreshLayout;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.fragment.FuckFragment;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2019-08-12.
 */
@Route(path = ArouterUtils.ACTIVITY_TAB2)
public class Tab2Activity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private Button mBtnSkip;
    private RefreshLayout mSwipeRefreshLayout;
    private boolean isExpanded;
    private PullDownHolder mPullDownHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.toolbar_tab);
        mAppBarLayout = findViewById(R.id.app_bar);
        mBtnSkip = findViewById(R.id.btn_skip);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mSwipeRefreshLayout = findViewById(R.id.refresh_root);
        mPullDownHolder = new PullDownHolder(this, mSwipeRefreshLayout);
        mSwipeRefreshLayout.setRefreshElastic(mPullDownHolder);

        mSwipeRefreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onRefreshComplete() {

            }

            @Override
            public void onSecondFloor() {

            }

            @Override
            public void onSecondFloorComplete() {

            }
        });
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                isExpanded = true;
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                isExpanded = false;
                mSwipeRefreshLayout.setEnabled(false);
            }
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 折叠吸顶状态
                mBtnSkip.setVisibility(View.VISIBLE);
            } else if (Math.abs(verticalOffset) == 0) {

            } else { // 展开状态
                mBtnSkip.setVisibility(View.GONE);
            }
            Log.i("xxxxxxxx", "verticalOffset：" + verticalOffset + "；TotalScrollRange：" + mAppBarLayout.getTotalScrollRange());
        });
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"职位", "求职者"};

        SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FuckFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
