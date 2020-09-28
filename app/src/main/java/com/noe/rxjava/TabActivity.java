package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.fragment.FuckFragment;
import com.noe.rxjava.refresh.SmartRefreshLayout;
import com.noe.rxjava.util.ArouterUtils;

@Route(path = ArouterUtils.ACTIVITY_TAB)
public class TabActivity extends BaseActivity {

    private View mHeader;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private Button mBtnSkip;
    private SmartRefreshLayout mSwipeRefreshLayout;
    private boolean isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
    }

    private void initView() {
        mHeader = findViewById(R.id.layout_user_info);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.toolbar_tab);
        mAppBarLayout = findViewById(R.id.app_bar);
        mBtnSkip = findViewById(R.id.btn_skip);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mViewPager);
        mSwipeRefreshLayout = findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(refreshLayout -> mSwipeRefreshLayout.finishRefresh(2000));
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
        private Context context;

        SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
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
