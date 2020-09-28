package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noe.rxjava.R;
import com.noe.rxjava.view.XRecyclerView;

import java.util.ArrayList;

import timber.log.Timber;


/**
 * Created by lijie24 on 2016/11/28.
 */

public class XRecyclerPageFragment extends Fragment {
    private Context mContext;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ArrayList<String> arrayList = new ArrayList<>();
    private AppBarLayout mAppBarLayout;
    private LinearLayoutManager mLinearLayoutManager;
    XRecyclerView recyclerView;
    private boolean isRefreshing;

    public static XRecyclerPageFragment newInstance(int page, AppBarLayout appBarLayout) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        XRecyclerPageFragment pageFragment = new XRecyclerPageFragment();
        pageFragment.mAppBarLayout = appBarLayout;
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_xrecycler, container, false);
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
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        recyclerView.setLayoutManager(mLinearLayoutManager);
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                load();
            }

            @Override
            public void onLoadMore() {

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;
            private int mScrolled;
            private int mPreviousDy;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if ((isRefreshing && firstVisiblePosition == 0) || (!isRefreshing&&firstVisiblePosition == 1)) {
                        if (mAppBarLayout != null) {
                            mAppBarLayout.setExpanded(true, true);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("DYDY-->", String.valueOf(dy));
                Timber.i("DY-->", String.valueOf(dy));
                totalDy -= dy;
                Log.i("totalDy-->", String.valueOf(totalDy));
//                mScrolled += dy;
//                // scrolled to the top with a little more velocity than a slow scroll e.g. flick/fling.
//                // Adjust 10 (vertical change of event) as you feel fit for you requirement
//                if(mScrolled == 0 && dy < -10 && mPreviousDy < 0) {
//                    mAppBarLayout.setExpanded(true, true);
//                }
//                mPreviousDy = dy;

            }
        });


//
//        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
//        SinaRefreshView headerView = new SinaRefreshView(mContext);
//        headerView.setArrowResource(R.drawable.anim_loading_view);
//        headerView.setTextColor(0xff745D5C);
////        TextHeaderView headerView = (TextHeaderView) View.inflate(this,R.layout.header_tv,null);
////        refreshLayout.setHeaderView(headerView);
//
//        refreshLayout.setProgressViewOffset(true,60,120);

//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshLayout.finishRefreshing();
//                    }
//                }, 2000);
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshLayout.finishLoadmore();
//                    }
//                }, 2000);
//            }
//        });
    }

    private void load() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.refreshComplete();
                isRefreshing = false;
            }
        },200000);
    }


//    @Override
//    protected void setScrollOnLayoutManager(int scrollY) {
//        mLinearLayoutManager.scrollToPositionWithOffset(0, -scrollY);
//    }

    int count;

    class MyRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<String> mArrayList;

        public MyRecyclerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            Log.d("xxxx-->", "onViewRecycled: " + holder.mTextView + ", position: " + holder.getAdapterPosition());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.i("onCreateViewHolder", "重用" + holder.mTextView.getTag());
            holder.mTextView.setText(arrayList.get(position));
            holder.mTextView.setTag(arrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i("onCreateViewHolder", "ViewHolder" + count++);
            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler);
        }
    }

}
