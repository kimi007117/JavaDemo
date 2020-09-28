package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noe.rxjava.R;
import com.noe.rxjava.pull.XRecyclerView;

import java.util.ArrayList;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;


/**
 * Created by lijie24 on 2016/11/28.
 */

public class PullRecyclerPageFragment extends Fragment implements ObservableFragment {
    private Context mContext;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ArrayList<String> arrayList = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private View mRootView;
    private View headerView;

    public static PullRecyclerPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PullRecyclerPageFragment pageFragment = new PullRecyclerPageFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_recycler_pull, container, false);
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
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView_x);
        headerView = View.inflate(mContext, R.layout.header_recycler_pull, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(arrayList);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public View getScrollTarget() {
        return mRecyclerView;
    }

    @Override
    public boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset) {
        return Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
    }

    int count = 0;

    class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> mArrayList;


        public MyRecyclerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                Log.i("onCreateViewHolder", "重用" + viewHolder.mTextView.getTag());
                viewHolder.mTextView.setText(arrayList.get(position));
                viewHolder.mTextView.setTag(arrayList.get(position));
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
            return mArrayList.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i("onCreateViewHolder" + mPage, "ViewHolder" + count++);
            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler);
        }
    }

}
