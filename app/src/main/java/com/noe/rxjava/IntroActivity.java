package com.noe.rxjava;

import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.noe.rxjava.view.ColorShades;

public class IntroActivity extends AppCompatActivity {
    private ConstraintLayout mRootLayout;
    private int colorBg[] = null;
    private ColorShades shades = null;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mRootLayout = (ConstraintLayout) findViewById(R.id.rl_root);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        colorBg = getResources().getIntArray(R.array.splash_bg);
        shades = new ColorShades();
        IntroPager introPager = new IntroPager(R.array.splash_icon, R.array.splash_desc);
        mViewPager.setAdapter(introPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);
                mRootLayout.setBackgroundColor(shades.generate());
//                applySelectedColor(shades.generate());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class IntroPager extends PagerAdapter {
        private String[] mDescs;
        private TypedArray mIcons;

        public IntroPager(int icoImage, int des) {
            mDescs = getResources().getStringArray(des);
            mIcons = getResources().obtainTypedArray(icoImage);
        }

        @Override
        public int getCount() {
            return mIcons.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemLayout = getLayoutInflater().inflate(R.layout.viewpager_intro, container, false);
            TextView mTextView = (TextView) itemLayout.findViewById(R.id.tv_desc);
            ImageView mImage = (ImageView) itemLayout.findViewById(R.id.iv_img);
            Button mButton = (Button) itemLayout.findViewById(R.id.btn_launch);
            mTextView.setText(mDescs[position]);
            mImage.setImageResource(mIcons.getResourceId(position, 0));
            if (position == getCount() - 1) {
                mButton.setVisibility(View.VISIBLE);
            } else {
                mButton.setVisibility(View.GONE);
            }
            container.addView(itemLayout);
            return itemLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
