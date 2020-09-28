package com.noe.rxjava.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.core.util.Logger;
import com.noe.rxjava.R;
import com.noe.rxjava.base.BaseFragment;

/**
 * Created by lijie on 2020/9/1.
 */
public class TwoFragment extends BaseFragment {

    private static final String TAG = TwoFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("two");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
    }
}
