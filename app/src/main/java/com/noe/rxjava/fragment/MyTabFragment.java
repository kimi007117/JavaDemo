package com.noe.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseFragment;
import com.noe.rxjava.event.OperateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lijie on 2019-10-11.
 */
public class MyTabFragment extends BaseFragment {

    private Button mButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("fragment-test", "MyTabFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment-test", "MyTabFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_my, container, false);
        initView(view);
        Log.i("fragment-test", "MyTabFragment onCreateView");
        return view;
    }

    private void initView(View view) {
        mButton = view.findViewById(R.id.btn_post);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateEvent event = new OperateEvent(1,"来自my tab");
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("fragment-test", "MyTabFragment onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("fragment-test", "MyTabFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("fragment-test", "MyTabFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment-test", "MyTabFragment onResume");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("fragment-test", "MyTabFragment setUserVisibleHint isVisibleToUser：" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("fragment-test", "MyTabFragment onHiddenChanged hidden：" + hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fragment-test", "MyTabFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment-test", "MyTabFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("fragment-test", "MyTabFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fragment-test", "MyTabFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("fragment-test", "MyTabFragment onDetach");
    }
}
