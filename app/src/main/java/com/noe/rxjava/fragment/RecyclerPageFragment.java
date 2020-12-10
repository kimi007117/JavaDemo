package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.core.util.ScreenUtils;
import com.noe.rxjava.R;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by lijie24 on 2016/11/28.
 */

public class RecyclerPageFragment extends Fragment {
    private Context mContext;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_HEIGHT = "ARG_HEIGHT";
    private int mPage;
    ArrayList<String> arrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private View mRootView;
    private View headerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mHeight;


    public static RecyclerPageFragment newInstance(int page, int height) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_HEIGHT, height);
        RecyclerPageFragment pageFragment = new RecyclerPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        mPage = getArguments().getInt(ARG_PAGE);
        mHeight = getArguments().getInt(ARG_HEIGHT);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View view) {
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            arrayList.add(((char) i) + "");
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        headerView = View.inflate(mContext, R.layout.header_recycler, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(arrayList);
        adapter.addHeaderView(headerView);
        mRecyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,layoutManager.getOrientation());
        itemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.bg_divider));
        mRecyclerView.addItemDecoration(itemDecoration);
        mHeight = mHeight - ScreenUtils.getStatusBarHeight(mContext);
        mSwipeRefreshLayout.setProgressViewOffset(true, mHeight, mHeight + ScreenUtils.dipToPixel(mContext, 40));

    }

    int count = 0;

    class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> mArrayList;
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_NORMAL = 1;
        private View mHeaderView;

        public MyRecyclerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new ViewHolder(mHeaderView);
            }

            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        public void addHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeaderView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && holder.getLayoutPosition() == 0) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_HEADER) {
                return;
            }
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                Log.i("onCreateViewHolder", "重用" + viewHolder.mTextView.getTag());
                viewHolder.mTextView.setText(arrayList.get(position - 1));
                viewHolder.mTextView.setTag(arrayList.get(position - 1));
            }

        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                Log.d("xxxx-->", "onViewRecycled: " + viewHolder.mTextView + ", position: " + holder.getAdapterPosition());
            }

        }

        @Override
        public int getItemCount() {
            return mHeaderView == null ? mArrayList.size() : mArrayList.size() + 1;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            if (itemView == headerView) {
                return;
            }
            Log.i("onCreateViewHolder" + mPage, "ViewHolder" + count++);
            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,mTextView.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
