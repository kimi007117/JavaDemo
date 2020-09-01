package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.fragment.PullRecyclerPageFragment;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.ObservablePagerAdapter;
@Route(path = ArouterUtils.ACTIVITY_PULL)
public class PullActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    private Bundle savedInstanceState;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_recycler);
        mTabLayout = (TabLayout) findViewById(R.id.bar_tab);
        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setOnClickListener(v -> {

        });
    }

    private void initData() {
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        simpleFragmentPagerAdapter.onRestoreInstanceState(savedInstanceState);
        simpleFragmentPagerAdapter.addFragment("最热", PullRecyclerPageFragment.newInstance(1));
        simpleFragmentPagerAdapter.addFragment("最新", PullRecyclerPageFragment.newInstance(2));
        mViewPager.setAdapter(simpleFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter implements ObservablePagerAdapter {

        private Context context;
        private final Map<Integer, String> mTags = new HashMap<>();
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
            for (Map.Entry<Integer, String> entry : mTags.entrySet()) {
                outState.putString(makeTagName(entry.getKey()), entry.getValue());
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        simpleFragmentPagerAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
