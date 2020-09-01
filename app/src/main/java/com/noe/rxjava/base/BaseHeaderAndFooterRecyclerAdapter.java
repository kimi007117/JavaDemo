package com.noe.rxjava.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展RecyclerView的Adapter<T>，添加header和footer，兼容GridLayoutManager和StaggeredGridLayoutManager
 * Created by lijie on 2018/12/17.
 */
public abstract class BaseHeaderAndFooterRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements IFooter {

    public static final int BASE_ITEM_TYPE_HEADER = 100000;
    public static final int BASE_ITEM_TYPE_FOOTER = 200000;

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

    /**
     * Header显示的Views
     */
    protected SparseArray<View> mHeaderViews = new SparseArray<>();
    /**
     * Footer显示的Views
     */
    protected SparseArray<View> mFooterViews = new SparseArray<>();


    public BaseHeaderAndFooterRecyclerAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
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
     * 获得数据
     */
    public List<T> getData() {
        if (null == mData) {
            mData = new ArrayList<>();
        }
        return mData;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        return doGetItemViewType(position - getHeaderCount());
    }

    public int doGetItemViewType(int position) {
        return super.getItemViewType(position - getHeaderCount());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return onCreateHeaderViewHolder(viewType);
        } else if (mFooterViews.get(viewType) != null) {
            return onCreateFooterViewHolder(viewType);
        }
        return doCreateViewHolder(parent, viewType);
    }

    public abstract BaseViewHolder<T> doCreateViewHolder(ViewGroup parent, int viewType);

    @NonNull
    protected BaseViewHolder<T> onCreateHeaderViewHolder(int viewType) {
        return new BaseViewHolder<>(mHeaderViews.get(viewType));
    }

    @NonNull
    protected BaseViewHolder<T> onCreateFooterViewHolder(int viewType) {
        return new BaseViewHolder<>(mFooterViews.get(viewType));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T task = mData.get(position);
        holder.onBind(task, position);
        holder.attach(position,task);
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getRealItemCount();
    }

    /**
     * 处理 GridLayoutManager
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * 处理 StaggeredGridLayoutManager
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public boolean isHeaderViewPos(int position) {
        return position < getHeaderCount();
    }

    public boolean isFooterViewPos(int position) {
        return position >= getHeaderCount() + getRealItemCount();
    }

    public int getRealItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public SparseArray<View> getHeaderViews() {
        return mHeaderViews;
    }

    public SparseArray<View> getFooterViews() {
        return mFooterViews;
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    @Override
    public int getFooterCount() {
        return mFooterViews.size();
    }
}
