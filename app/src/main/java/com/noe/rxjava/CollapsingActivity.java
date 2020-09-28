package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.fragment.FatherFragment;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2019-09-10.
 */
@Route(path = ArouterUtils.ACTIVITY_COLLAPSING)
public class CollapsingActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing);
        mTabLayout = findViewById(R.id.toolbar_tab);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mViewPager);
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
            return FatherFragment.newInstance(position + 1);
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
