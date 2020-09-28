package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.noe.rxjava.fragment.PageFragment;

/**
 * Created by 58 on 2016/8/5.
 */
public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),this));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {

    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[]{"工友圈"};
        private Context context;

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
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
