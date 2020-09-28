package com.noe.rxjava.base;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * 公共的viewholder基类
 * Created by lijie on 2018/12/17.
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;
    protected int position;
    protected T data;

    public BaseViewHolder(View itemView) {
        this(itemView, null, null);
    }

    public BaseViewHolder(View itemView, OnItemClickListener<T> onItemClickListener) {
        this(itemView, onItemClickListener, null);
    }

    public BaseViewHolder(View itemView, OnItemClickListener<T> onItemClickListener, OnItemLongClickListener<T> onItemLongClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public final void attach(int position, T data) {
        this.position = position;
        this.data = data;
    }

    public void onBind(T data, int position) {
    }

    protected <T extends View> T findViewById(@IdRes int viewId) {
        return this.itemView.findViewById(viewId);
    }

    public final void onClick(View v) {
        if (this.onItemClickListener != null) {
            this.onItemClickListener.onItemClick(v, this.position, this.data);
        }
    }

    public final boolean onLongClick(View v) {
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.onItemLongClick(v, position, data);
        }
        return false;
    }

    public final void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public final OnItemClickListener<T> getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public final void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
    }

    public final OnItemLongClickListener<T> getOnItemLongClickListener() {
        return this.onItemLongClickListener;
    }
}
