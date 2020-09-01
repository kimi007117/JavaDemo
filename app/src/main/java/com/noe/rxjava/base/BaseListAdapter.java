package com.noe.rxjava.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView和GridView的基类adapter
 * Created by lijie on 2018/12/17.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * View打气筒
     */
    protected LayoutInflater mInflater;

    /**
     * 数据
     */
    private List<T> mData;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
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

    /**
     * 清除数据
     */
    public void removeAll() {
        if (mData != null && !mData.isEmpty()) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param absListView 要更新的absListView
     * @param position    要更新的位置
     */
    public void notifyItemChanged(AbsListView absListView, T data, int position) {
        // 第一个可见的位置
        int firstVisiblePosition = absListView.getFirstVisiblePosition();
        // 最后一个可见的位置
        int lastVisiblePosition = absListView.getLastVisiblePosition();
        // 当前position 如果是ListView包括头部
        int currentPosition = position;

        if (absListView instanceof ListView) {
            currentPosition = position + ((ListView) absListView).getHeaderViewsCount();
        }

        if (currentPosition >= firstVisiblePosition && currentPosition <= lastVisiblePosition) {
            // 获取指定位置view对象
            View view = absListView.getChildAt(currentPosition - firstVisiblePosition);
            if (data != null) {
                getData().set(position, data);
            }
            getView(position, view, absListView);
        }
    }

    /**
     * 获得数据
     */
    public List<T> getData() {
        if (null == mData) {
            mData = new ArrayList<>();
        }
        return mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            Log.i("xxxxxxxx", String.valueOf(position));
            holder = onCreateViewHolder(parent, getItemViewType(position));
            convertView.setTag(holder);
        } else {
            Log.i("xxxx", String.valueOf(position));
            holder = (BaseViewHolder) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    /**
     * 创建viewholder
     */
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (mData == null) {
            return;
        }
        T task = mData.get(position);
        holder.onBind(task, position);
    }
}
