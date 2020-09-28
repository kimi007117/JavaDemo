package com.noe.rxjava.base;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 2019-11-19.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected final LayoutInflater mInflater;
    protected List<T> mData;
    protected Context mContext;

    public BaseRecyclerAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mData = data;
        if (null == this.mData) {
            this.mData = new ArrayList<>();
        }
    }

    public LayoutInflater getInflater() {
        return this.mInflater;
    }

    public void setData(@Nullable List<T> data) {
        this.mData = data;
        if (this.mData == null) {
            this.mData = new ArrayList<>();
        }
    }

    public void addData(@Nullable List<T> arrayList) {
        if (this.mData == null) {
            this.mData = new ArrayList<>();
        }

        if (null != arrayList) {
            this.mData.addAll(arrayList);
        }
    }

    public void clear() {
        if (this.mData != null) {
            this.mData.clear();
        }
    }

    public List<T> getData() {
        return this.mData;
    }

    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        T itemData = this.mData.get(position);
        viewHolder.attach(position, itemData);
        viewHolder.onBind(itemData, position);
    }

    public int getItemCount() {
        int size = this.mData == null ? 0 : this.mData.size();
        return size;
    }
}
