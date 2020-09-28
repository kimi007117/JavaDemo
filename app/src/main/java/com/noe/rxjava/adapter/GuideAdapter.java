package com.noe.rxjava.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noe.rxjava.R;

import java.util.List;

/**
 * 成功案例 滚动adpter
 * Created by lijie on 2019-07-19.
 */
public class GuideAdapter extends PagerAdapter {

    private Context mContext;

    private List<Integer> mInfoList;

    public GuideAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Integer> list) {
        this.mInfoList = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_banner, null);
        ImageView mCompanyPic = view.findViewById(R.id.iv_pic);
        Integer integer = mInfoList.get(position % getRealCount());
        mCompanyPic.setImageResource(integer);
        container.addView(view);
        return view;
    }

    public int getRealCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    @Override
    public int getCount() {
        int realCount = getRealCount();
        if (realCount <= 1) {
            return realCount;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
