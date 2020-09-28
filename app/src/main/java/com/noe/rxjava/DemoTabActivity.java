package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.fragment.CaseTabFragment;
import com.noe.rxjava.fragment.DemoTabFragment;
import com.noe.rxjava.fragment.EffectTabFragment;
import com.noe.rxjava.fragment.MyTabFragment;
import com.noe.rxjava.fragment.VipTabFragment;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lijie on 2019-10-11.
 */
@Route(path = ArouterUtils.ACTIVITY_DEMO_TAB)
public class DemoTabActivity extends BaseActivity implements TabHost.OnTabChangeListener {

    /**
     * 客户数据
     */
    public final static String TAB_EFFECT = "effect";

    /**
     * 客户案例
     */
    public final static String TAB_CASE = "case";

    /**
     * 推广演示
     */
    public final static String TAB_DEMO = "demo";

    /**
     * 会员套餐
     */
    public final static String TAB_VIP = "vip";

    /**
     * 我的
     */
    public final static String TAB_MY = "my";

    /**
     * Tab 布局实例化工具
     */
    private LayoutInflater mLayoutInflater;

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;

    private TabHost mTabHost;

    // 当前tag
    private String mCurrentTag = null;

    private Fragment mCurrentFragment;

    private MyTabFragment mMyTabFragment;
    private CaseTabFragment mCaseTabFragment;
    private EffectTabFragment mEffectTabFragment;
    private DemoTabFragment mDemoTabFragment;
    private VipTabFragment mVipTabFragment;

    /**
     * Tab 标签
     */
    public static List<String> mTabTag = new ArrayList<>(Arrays.asList(TAB_EFFECT, TAB_CASE, TAB_DEMO, TAB_VIP, TAB_MY));

    /**
     * Tab 标题
     */
    public static String[] mTabTitle;

    /**
     * Tab 图标
     */
    private List<Integer> mTabIcon = new ArrayList<>(Arrays.asList(R.drawable.tab_effect_selector, R.drawable.tab_case_selector, R.drawable.tab_demo_selector, R.drawable.tab_vip_selector, R.drawable.tab_my_selector));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mCurrentTag = savedInstanceState.getString("currentTab");
            if (mCurrentTag != null) {
                this.mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, mCurrentTag);
            }
        }
        setContentView(R.layout.activity_demo_tab);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        mFragmentManager = getSupportFragmentManager();
        mTabHost = findViewById(R.id.tab_host);
        initTabs();
    }

    /**
     * 初始化tab
     */
    private void initTabs() {
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();
        mTabTitle = getResources().getStringArray(R.array.main_tab_text);
        for (int i = 0; i < mTabTag.size(); i++) {
            mTabHost.addTab(mTabHost.newTabSpec(mTabTag.get(i)).setIndicator(getTabItemView(i)).setContent(new DummyTabFactory(mContext)));
        }
        mTabHost.setCurrentTab(mTabTag.indexOf(TAB_EFFECT));
    }

    /**
     * 底部tab的view
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item, null);
        TextView text = view.findViewById(R.id.tab_item_text);
        ImageView imageView = view.findViewById(R.id.tab_item_icon);
        imageView.setImageResource(mTabIcon.get(index));
        text.setText(mTabTitle[index]);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (TextUtils.isEmpty(tabId)) {
            return;
        }
        mCurrentTag = tabId;
        Fragment fragment = null;
        if (TAB_EFFECT.equals(tabId)) {
            if (mEffectTabFragment == null) {
                mEffectTabFragment = new EffectTabFragment();
                mEffectTabFragment.onAttach(this);
            }
            fragment = mEffectTabFragment;
        } else if (TAB_CASE.equals(tabId)) {
            if (mCaseTabFragment == null) {
                mCaseTabFragment = new CaseTabFragment();
                mCaseTabFragment.onAttach(this);
            }
            fragment = mCaseTabFragment;
        } else if (TAB_DEMO.equals(tabId)) {
            if (mDemoTabFragment == null) {
                mDemoTabFragment = new DemoTabFragment();
                mDemoTabFragment.onAttach(this);
            }
            fragment = mDemoTabFragment;
        } else if (TAB_VIP.equals(tabId)) {
            if (mVipTabFragment == null) {
                mVipTabFragment = new VipTabFragment();
                mVipTabFragment.onAttach(this);
            }
            fragment = mVipTabFragment;
        } else if (TAB_MY.equals(tabId)) {
            if (mMyTabFragment == null) {
                mMyTabFragment = new MyTabFragment();
                mMyTabFragment.onAttach(this);
            }
            fragment = mMyTabFragment;
        }
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }

    /**
     * 切换fragment
     */
    private void replaceFragment(Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mCurrentFragment != null && !mCurrentFragment.isDetached()) {
            mCurrentFragment.setUserVisibleHint(false);
            fragmentTransaction.detach(mCurrentFragment);
        }

        if (targetFragment.isDetached()) {
            fragmentTransaction.attach(targetFragment);
        } else {
            fragmentTransaction.add(android.R.id.tabcontent, targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        mCurrentFragment = targetFragment;
    }

    /**
     * 切换fragment
     */
    private void replaceFragment2(Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mCurrentFragment != null) {
            mCurrentFragment.setUserVisibleHint(false);
            fragmentTransaction.hide(mCurrentFragment);
        }

        if (targetFragment.isAdded()) {
            fragmentTransaction.show(targetFragment);
        } else {
            fragmentTransaction.add(android.R.id.tabcontent, targetFragment);
        }
        targetFragment.setUserVisibleHint(true);
        fragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        mCurrentFragment = targetFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            //todo 添加容错判断，onSaveInstanceState 中fragment被异常收回了。index才有可能为-1的问题
            super.onSaveInstanceState(outState);
            if (outState != null) {
                outState.putString("currentTab", this.mCurrentTag);
                if (mCurrentFragment != null) {
                    getSupportFragmentManager().putFragment(outState, mCurrentTag, mCurrentFragment);
                }
            }
        } catch (Exception e) {

        }
        invokeFragmentManagerNoteStateNotSaved();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                if (mCurrentFragment == null) {
                    this.mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, mCurrentTag);
                }
                if (mTabHost != null) {
                    mTabHost.setCurrentTabByTag(savedInstanceState.getString("currentTab"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除当前Fragment所属的所有子Fragment
     */
    private void clearFragments() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getFragments().size() > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fm.getFragments()) {
                ft.remove(fragment);
            }
            ft.commitAllowingStateLoss();
        }
    }

    private static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
}
