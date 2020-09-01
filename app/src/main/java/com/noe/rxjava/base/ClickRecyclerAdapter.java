package com.noe.rxjava.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lijie on 2019-11-19.
 */
public abstract class ClickRecyclerAdapter <T> extends HeaderAndFooterRecyclerAdapter<T> {

    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;

    public ClickRecyclerAdapter(Context context) {
        this(context, null, null);
    }

    public ClickRecyclerAdapter(Context context, OnItemClickListener<T> onItemClickListener) {
        this(context, onItemClickListener, null);
    }

    public ClickRecyclerAdapter(Context context, OnItemClickListener<T> onItemClickListener, OnItemLongClickListener<T> onItemLongClickListener) {
        super(context);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public ClickRecyclerAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder<T> doCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder<T> viewHolder = onCreateNormalViewHolder(parent, viewType);

        if (null == viewHolder.getOnItemClickListener()) {
            viewHolder.setOnItemClickListener(onItemClickListener);
        }

        if (null == viewHolder.getOnItemLongClickListener()) {
            viewHolder.setOnItemLongClickListener(onItemLongClickListener);
        }

        viewHolder.itemView.setOnClickListener(viewHolder);
        viewHolder.itemView.setOnLongClickListener(viewHolder);

        return viewHolder;
    }

    public abstract BaseViewHolder<T> onCreateNormalViewHolder(ViewGroup parent, int viewType);


    /**
     * 1. 需要在{@link #onCreateViewHolder}中调用{@link BaseViewHolder#setOnItemClickListener(OnItemClickListener)}
     * 才能将 {@link OnItemClickListener} 传递给 {@link BaseViewHolder}
     * <p>
     * 2. 需要在 {@link BaseViewHolder#BaseViewHolder(View)} 中对需要点击的view调用 {@link View#setOnClickListener(View.OnClickListener)}
     * 设置点击事件，才能正常触发{@link OnItemClickListener}
     *
     * @param listener
     */
    public final void setOnItemClickListener(OnItemClickListener<T> listener) {
        onItemClickListener = listener;
    }

    /**
     * 1. 需要在{@link #onCreateViewHolder}中调用{@link BaseViewHolder#setOnItemLongClickListener(OnItemLongClickListener)}
     * 才能将 {@link OnItemLongClickListener} 传递给 {@link BaseViewHolder}
     * <p>
     * 2. 需要在 {@link BaseViewHolder#BaseViewHolder(View)} 中对需要点击的view调用 {@link View#setOnLongClickListener(View.OnLongClickListener)}
     * 设置点击事件，才能正常触发{@link OnItemLongClickListener}
     *
     * @param listener
     */
    public final void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        onItemLongClickListener = listener;
    }

}
