package com.noe.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.util.ArouterUtils;

@Route(path = ArouterUtils.ACTIVITY_SLIDE)
public class SlideFeatureActivity extends AppCompatActivity {
    private static final int[] mResources = new int[]{R.drawable.leading_bg_one, R.drawable.leading_bg_two, R.drawable.leading_bg_three};
    private ImageView[] mDots;
    private ViewPager mViewPagerSlide;
    private Button mButtonGo;
    private LinearLayout mDotContainer;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_slide_feature);
        mViewPagerSlide = (ViewPager) findViewById(R.id.slide_view_pager);
        mDotContainer = (LinearLayout) findViewById(R.id.ll_dot_container);
        mButtonGo = (Button) findViewById(R.id.btn_go);
        initDots();
        mViewPagerSlide.setAdapter(new SlideAdapter(mResources));
        mViewPagerSlide.addOnPageChangeListener(mOnPageChangeListener);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDots() {
        mDots = new ImageView[mResources.length];
        for (int i = 0; i < mResources.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.slide_dot_selector, null));
            imageView.setEnabled(false);
            imageView.setPadding(0, 0, 20, 0);
            mDots[i] = imageView;
            mDotContainer.addView(imageView);
        }
        mDots[0].setEnabled(true);
    }

    /**
     * 设置当前指示点
     *
     * @param position 当前位置
     */
    private void setCurrentDot(int position) {
        if (position < 0 || position >= mDots.length) {
            return;
        }
        for (ImageView mDot : mDots) {
            mDot.setEnabled(false);
        }
        mDots[position].setEnabled(true);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mResources.length - 1) {
                mButtonGo.setVisibility(View.VISIBLE);
            } else {
                mButtonGo.setVisibility(View.GONE);
            }
            setCurrentDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class SlideAdapter extends PagerAdapter {
        private int[] resources;

        SlideAdapter(int[] resources) {
            this.resources = resources;
        }

        @Override
        public int getCount() {
            return resources == null ? 0 : resources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.item_pager, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_slide);
            if (resources != null && resources.length > 0) {
                imageView.setImageResource(resources[position]);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
