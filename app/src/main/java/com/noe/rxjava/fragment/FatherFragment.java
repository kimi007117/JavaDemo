package com.noe.rxjava.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseHeaderAndFooterRecyclerAdapter;
import com.noe.rxjava.base.BaseViewHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by lijie on 2019-07-25.
 */
public class FatherFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private View headerView;
    private boolean isExpanded;

    private ArrayList<String> mArrayList = new ArrayList<>();

    public static FatherFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FatherFragment fragment = new FatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean getExpanded() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FuckFragment) {
            return ((FuckFragment) parentFragment).getExpanded();
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_father, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initData();
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        setMaxFlingVelocity(mRecyclerView, 16000);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        MyAdapter adapter = new MyAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        adapter.setData(mArrayList);
        adapter.notifyDataSetChanged();
    }


    private class MyAdapter extends BaseHeaderAndFooterRecyclerAdapter<String> {

        MyAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder<String> doCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(view);
        }

        class ViewHolder extends BaseViewHolder<String> {

            TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv_recycler);
            }

            @Override
            public void onBind(String data, int position) {
                super.onBind(data, position);
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                mTextView.setText(data);
                itemView.setOnClickListener(v -> Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("xxxxx", isVisibleToUser + "");
        if (mRecyclerView != null && getExpanded()) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    private void setMaxFlingVelocity(RecyclerView recyclerView, int velocity) {
        try {
            Field field = recyclerView.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recyclerView, velocity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
