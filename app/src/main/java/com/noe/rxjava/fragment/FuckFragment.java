package com.noe.rxjava.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.noe.rxjava.R;
import com.noe.rxjava.TabActivity;

/**
 * Created by lijie on 2019-07-25.
 */
public class FuckFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private Activity mContext;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RelativeLayout mTipsLayout;
    private ImageButton mBtnClose;

    public static FuckFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FuckFragment fragment = new FuckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuck, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initData();
    }

    private void initView(View view) {
        mViewPager = view.findViewById(R.id.view_pager);
        mTabLayout = view.findViewById(R.id.toolbar_tab);
        mTipsLayout = view.findViewById(R.id.layout_tips);
        mBtnClose = view.findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(v -> mTipsLayout.setVisibility(View.GONE));
    }

    private void initData() {
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), mContext));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("uuuuuuu","View.GONE");
                mBtnClose.setVisibility(View.GONE);
            }
        },3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("uuuuuuu","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("uuuuuuu","onDestroy");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("yyyyyy",isVisibleToUser + "");

    }

    public boolean getExpanded(){
        if (getActivity() instanceof TabActivity){
            return  ((TabActivity) getActivity()).getExpanded();
        }
        return true;
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"投递", "下载", "视频面试"};
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
