package com.noe.rxjava.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noe.rxjava.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 2019-07-20.
 */
public class BannerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mData;

    private Context mContext;

    public BannerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        int realCount = getRealCount();
        if (realCount <= 1) {
            return realCount;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * 设置数据
     */
    public void setData(List<T> arrayList) {
        if (arrayList == null) {
            return;
        }
        mData = arrayList;
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    public void addData(List<T> arrayList) {
        if (arrayList == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(arrayList);
        notifyDataSetChanged();
    }

    public int getRealCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).mImageView.setImageResource((Integer) mData.get(position % getRealCount()));
        }
    }

    class BannerViewHolder<T> extends RecyclerView.ViewHolder {

        ImageView mImageView;

        BannerViewHolder(@NonNull View view) {
            super(view);
            mImageView = view.findViewById(R.id.iv_pic);
        }

    }

}
