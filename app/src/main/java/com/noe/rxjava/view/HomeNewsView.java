package com.noe.rxjava.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noe.rxjava.R;

import java.util.ArrayList;

/**
 * Created by lijie24 on 2017/8/7.
 */

public class HomeNewsView extends LinearLayout {
    private LayoutInflater inflater;
    private Context mContext;
    private AdapterViewFlipper mViewFlipper;
    private ArrayList<String> news = new ArrayList<>();

    public HomeNewsView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public HomeNewsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public HomeNewsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        Log.i("HomeNewsView", "initView");
        inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.local_news_content, this, true);
        mViewFlipper = (AdapterViewFlipper) findViewById(R.id.marquee_view);
        news.add("Android仿淘宝头条基于TextView实现上下滚动通知效果");
        news.add("IOS仿淘宝头条基于TextView实现上下滚动通知效果");
        news.add("JS仿淘宝头条基于TextView实现上下滚动通知效果");
        mViewFlipper.setAdapter(new TextAdapter());
        // 获取当前的item的position  mViewFlipper.getDisplayedChild()
    }

    private class TextAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FlipperViewHolder holder;
            if (convertView == null) {
                holder = new FlipperViewHolder();
                convertView = inflater.inflate(R.layout.local_news_item, null);
                holder.text = (TextView) convertView.findViewById(R.id.tv_news);
                convertView.setTag(holder);
            } else {
                holder = (FlipperViewHolder) convertView.getTag();
            }
            holder.text.setText(news.get(position));
            return convertView;
        }

        class FlipperViewHolder {
            TextView text;
        }
    }

}
