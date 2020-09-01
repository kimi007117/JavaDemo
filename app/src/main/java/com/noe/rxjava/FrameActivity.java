package com.noe.rxjava;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.base.RxApplication;
import com.noe.rxjava.fragment.JobFragment;
import com.noe.rxjava.fragment.ResumeFragment;
import com.noe.rxjava.util.ArouterUtils;

@Route(path = ArouterUtils.ACTIVITY_FRAME)
public class FrameActivity extends BaseActivity {

    private static final int NUM_ITEMS = 2;

    private LinearLayout mJobLayout;
    private LinearLayout mResumeLayout;
    private Fragment[] mFragments;

    private RadioGroup mRadioGroup;
    private FragmentManager mFragmentManager;
    private AppBarLayout mAppBarLayout;
    private Button mBtnSkip;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mJobLayout = findViewById(R.id.layout_job);
        mResumeLayout = findViewById(R.id.layout_resume);
        mRadioGroup = findViewById(R.id.rg_container);
        mAppBarLayout = findViewById(R.id.app_bar);
        mBtnSkip = findViewById(R.id.btn_skip);
        mFragments = new Fragment[]{JobFragment.newInstance(), ResumeFragment.newInstance()};
        mSwipeRefreshLayout = findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000));
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRadioGroup.getLayoutParams();
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 折叠吸顶状态
                mBtnSkip.setVisibility(View.VISIBLE);
                params.setMargins(dip2px(15), 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else { // 展开状态
                mBtnSkip.setVisibility(View.GONE);
                params.setMargins(0, 0, 0, 0);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
                    params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                } else {
                    params.getRules()[RelativeLayout.CENTER_IN_PARENT] = 0;
                    params.getRules()[RelativeLayout.ALIGN_PARENT_LEFT] = 0;
                }
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            Log.i("xxxxxxxx", "verticalOffset：" + verticalOffset + "；TotalScrollRange：" + mAppBarLayout.getTotalScrollRange());
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_job) {
                    replaceFragment(R.id.container, mFragments[0]);
                    mJobLayout.setVisibility(View.VISIBLE);
                    mResumeLayout.setVisibility(View.GONE);

                } else if (checkedId == R.id.rb_resume) {
                    replaceFragment(R.id.container, mFragments[1]);
                    mJobLayout.setVisibility(View.GONE);
                    mResumeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        ((RadioButton) findViewById(R.id.rb_job)).setChecked(true);
    }

    /**
     * 切换fragment
     */
    private void replaceFragment(int containerViewId, Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            if (mCurrentFragment != null) {
                fragmentTransaction.hide(mCurrentFragment);
            }
            if (targetFragment instanceof JobFragment) {

            } else if (targetFragment instanceof ResumeFragment) {

            }
            fragmentTransaction.add(containerViewId, targetFragment).commitAllowingStateLoss();
        } else {
            fragmentTransaction.hide(mCurrentFragment).show(targetFragment).commitAllowingStateLoss();
        }
        mFragmentManager.executePendingTransactions();
        mCurrentFragment = targetFragment;
    }

    private static int dip2px(float dpValue) {
        final float scale = RxApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
