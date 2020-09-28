package com.noe.rxjava;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.fragment.XRecyclerPageFragment;
import com.noe.rxjava.parallaxviewpager.ParallaxFragmentPagerAdapter;
import com.noe.rxjava.parallaxviewpager.ParallaxViewPagerBaseActivity;
import com.noe.rxjava.util.ArouterUtils;

@Route(path = ArouterUtils.ACTIVITY_PARALLAX)
public class ParallaxActivity extends ParallaxViewPagerBaseActivity {

    private ViewPager mViewPager;
    private ImageView mBanner;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
        mBanner = (ImageView) findViewById(R.id.banner);
        mHeader = findViewById(R.id.header);
        mNumFragments = 2;
        setupAdapter();
    }

    @Override
    protected void initValues() {
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + tabHeight;
    }

    @Override
    protected void scrollHeader(int scrollY) {
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeader.setTranslationY(translationY);
        mBanner.setTranslationY(-translationY / 3);
    }

    @Override
    protected void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments);
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private static class ViewPagerAdapter extends ParallaxFragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm, int numFragments) {
            super(fm, numFragments);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = XRecyclerPageFragment.newInstance(0, null);
                    break;

                case 1:
                    fragment = XRecyclerPageFragment.newInstance(1, null);
                    break;

                case 2:
                    fragment = XRecyclerPageFragment.newInstance(2, null);
                    break;

                case 3:
                    fragment = XRecyclerPageFragment.newInstance(3, null);
                    break;

                default:
                    throw new IllegalArgumentException("Wrong page given " + position);
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ScrollView";

                case 1:
                    return "ScrollView";

                case 2:
                    return "ListView";

                case 3:
                    return "RecyclerView";

                default:
                    throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
            }
        }
    }
}
