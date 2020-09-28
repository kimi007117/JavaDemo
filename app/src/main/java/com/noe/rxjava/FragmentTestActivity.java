package com.noe.rxjava;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.fragment.OneFragment;
import com.noe.rxjava.fragment.ThreeFragment;
import com.noe.rxjava.fragment.TwoFragment;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020/9/1.
 */
@Route(path = ArouterUtils.ACTIVITY_TEST_FRAGMENT)
public class FragmentTestActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private Button mBtnOne;
    private Button mBtnTwo;
    private Button mBtnThree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        mFragmentManager = getSupportFragmentManager();
        mBtnOne = findViewById(R.id.btn_one);
        mBtnTwo = findViewById(R.id.btn_two);
        mBtnThree = findViewById(R.id.btn_three);
        OneFragment oneFragment = new OneFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, oneFragment)
                .commitAllowingStateLoss();
        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBtnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoFragment twoFragment = new TwoFragment();
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, twoFragment)
                        .commitAllowingStateLoss();
            }
        });
        mBtnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreeFragment threeFragment = new ThreeFragment();
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, threeFragment)
                        .commitAllowingStateLoss();
            }
        });

    }
}
