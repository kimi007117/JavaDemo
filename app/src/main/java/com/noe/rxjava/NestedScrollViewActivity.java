package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.adapter.CustomerItemAdapter;
import com.noe.rxjava.adapter.WCFeedAdapter;
import com.noe.rxjava.adapter.RecyclerItemAdapter;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import java.util.ArrayList;

/**
 * Created by lijie on 2019-11-18.
 */
@Route(path = ArouterUtils.ACTIVITY_NESTEDSCROLLVIEW_NAVIGATION)
public class NestedScrollViewActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView1;
    private ViewGroup mRootView;

    private CustomerItemAdapter mCustomerItemAdapter;
    private View mHeaderView;

    private ArrayList<String> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycle_view);
        mRootView = findViewById(R.id.root_view);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_recycler_view, mRootView, false);
        mRecyclerView1 = mHeaderView.findViewById(R.id.recyclerView);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        RecyclerItemAdapter recyclerItemAdapter = new RecyclerItemAdapter(this);
        recyclerItemAdapter.setOnItemClickListener((view, position, data) -> Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("c");
        recyclerItemAdapter.setData(arrayList);
        mRecyclerView1.setAdapter(recyclerItemAdapter);
        mCustomerItemAdapter = new CustomerItemAdapter(this);
        mCustomerItemAdapter.setData(mArrayList);
        mRecyclerView.setAdapter(mCustomerItemAdapter);
        mCustomerItemAdapter.addHeaderView(mHeaderView);
        mCustomerItemAdapter.notifyDataSetChanged();
        new WCFeedAdapter(mContext);
    }
}
