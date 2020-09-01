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

import com.noe.rxjava.R;
import com.noe.rxjava.adapter.CustomerItemAdapter;
import com.noe.rxjava.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by lijie on 2019-10-11.
 */
public class EffectTabFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private ArrayList<String> mArrayList = new ArrayList<>();
    private CustomerItemAdapter mCustomerItemAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("fragment-test", "EffectTabFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment-test", "EffectTabFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_effect, container, false);
        initView(view);
        initData();
        Log.i("fragment-test", "EffectTabFragment onCreateView");
        return view;
    }

    private void initView(View view) {
        mTextView = view.findViewById(R.id.tv_title);
        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCustomerItemAdapter = new CustomerItemAdapter(getActivity());
        mRecyclerView.setAdapter(mCustomerItemAdapter);
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mArrayList.add(((char) i) + "");
        }
        mCustomerItemAdapter.setData(mArrayList);
        mCustomerItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("fragment-test", "EffectTabFragment onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("fragment-test", "EffectTabFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("fragment-test", "EffectTabFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment-test", "EffectTabFragment onResume");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("fragment-test", "EffectTabFragment setUserVisibleHint isVisibleToUser：" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("fragment-test", "EffectTabFragment onHiddenChanged hidden：" + hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fragment-test", "EffectTabFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment-test", "EffectTabFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("fragment-test", "EffectTabFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fragment-test", "EffectTabFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("fragment-test", "EffectTabFragment onDetach");
    }
}
