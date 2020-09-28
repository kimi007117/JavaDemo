package com.noe.rxjava.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lijie on 2019-11-19.
 */
public abstract class HeaderAndFooterRecyclerAdapter <T> extends BaseRecyclerAdapter<T> {

    public static final int BASE_ITEM_TYPE_HEADER = 100000;
    public static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;


    public HeaderAndFooterRecyclerAdapter(Context context) {
        super(context);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    public HeaderAndFooterRecyclerAdapter(Context context, List<T> data) {
        super(context, data);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public final BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return onCreateHeaderViewHolder(viewType);
        } else if (mFooterViews.get(viewType) != null) {
            return onCreateFooterViewHolder(viewType);
        }
        return doCreateViewHolder(parent, viewType);
    }

    @NonNull
    protected BaseViewHolder<T> onCreateHeaderViewHolder(int viewType) {
        return new BaseViewHolder<>(mHeaderViews.get(viewType));
    }

    @NonNull
    protected BaseViewHolder<T> onCreateFooterViewHolder(int viewType) {
        return new BaseViewHolder<>(mFooterViews.get(viewType));
    }

    /**
     * should be override
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract BaseViewHolder<T> doCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {

        if (isHeaderView(position)) {
            onBindHeaderViewHolder(holder, position);
            return;
        }
        if (isFooterView(position)) {
            onBindFooterViewHolder(holder, position);
            return;
        }
        onBindNormalViewHolder(holder, position, position - getHeadersCount());

    }

    protected void onBindHeaderViewHolder(BaseViewHolder holder, int position) {
        // 供子类覆盖
    }

    protected void onBindFooterViewHolder(BaseViewHolder holder, int position) {
        // 供子类覆盖
    }

    protected void onBindNormalViewHolder(BaseViewHolder holder, int position, int realPosition) {
        super.onBindViewHolder(holder, realPosition);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getNormalCount());
        }
        return doGetItemViewType(position - getHeadersCount());
    }

    public int doGetItemViewType(int position) {
        return super.getItemViewType(position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getNormalCount();
    }

    public int getRealItemCount() {
        return getItemCount() - getHeadersCount() - getFootersCount();
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
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    public boolean isFooterView(int position) {
        return position >= getHeadersCount() + getNormalCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public boolean removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index != -1) {
            mHeaderViews.removeAt(index);
            return true;
        }
        return false;
    }

    public boolean removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index != -1) {
            mFooterViews.removeAt(index);
            return true;
        }
        return false;
    }

    public SparseArray<View> getHeaderViews() {
        return mHeaderViews;
    }

    public SparseArray<View> getFooterViews() {
        return mFooterViews;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    public int getNormalCount() {
        return super.getItemCount();
    }


}
