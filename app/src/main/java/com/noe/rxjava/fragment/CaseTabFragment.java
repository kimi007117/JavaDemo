package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noe.rxjava.R;
import com.noe.rxjava.adapter.CustomerItemAdapter;
import com.noe.rxjava.base.BaseFragment;
import com.noe.rxjava.event.OperateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by lijie on 2019-10-11.
 */
public class CaseTabFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private ArrayList<String> mArrayList = new ArrayList<>();
    private CustomerItemAdapter mCustomerItemAdapter;
    private boolean isInit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("fragment-test", "CaseTabFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.i("fragment-test", "CaseTabFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_case, container, false);
        initView(view);
        initData();
        Log.i("fragment-test", "CaseTabFragment onCreateView");
        return view;
    }

    private void initView(View view) {
        mTextView = view.findViewById(R.id.tv_title);
        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData() {
        if (isInit) {
            mRecyclerView.setAdapter(mCustomerItemAdapter);
        } else {
            mTextView.setText("呵呵呵");
            for (int i = 'A'; i <= 'Z'; i++) {
                mArrayList.add(((char) i) + "");
            }
            mCustomerItemAdapter = new CustomerItemAdapter(getActivity());
            mCustomerItemAdapter.setData(mArrayList);
            mRecyclerView.setAdapter(mCustomerItemAdapter);
        }
        isInit = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OperateEvent event) {
        if (event != null && event.code == 1) {
            String strPost = (String) event.data;
            mTextView.setText(strPost);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("fragment-test", "CaseTabFragment onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("fragment-test", "CaseTabFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("fragment-test", "CaseTabFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment-test", "CaseTabFragment onResume");
        if (getUserVisibleHint()) {
            Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("fragment-test", "CaseTabFragment setUserVisibleHint isVisibleToUser：" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("fragment-test", "CaseTabFragment onHiddenChanged hidden：" + hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fragment-test", "CaseTabFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment-test", "CaseTabFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("fragment-test", "CaseTabFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.i("fragment-test", "CaseTabFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("fragment-test", "CaseTabFragment onDetach");
    }

}
