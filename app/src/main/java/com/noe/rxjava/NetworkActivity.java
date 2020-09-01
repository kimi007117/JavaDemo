package com.noe.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.network.NetStateChangeObserver;
import com.noe.rxjava.network.NetStateChangeReceiver;
import com.noe.rxjava.network.NetworkType;
import com.noe.rxjava.util.ArouterUtils;

/**
 * Created by lijie on 2020/7/28.
 */
@Route(path = ArouterUtils.ACTIVITY_NETWORK)
public class NetworkActivity extends BaseActivity implements NetStateChangeObserver {

    private static final String TAG = "NetworkActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetStateChangeReceiver.registerReceiver(this);
        setContentView(R.layout.activity_network);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetStateChangeReceiver.unRegisterObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateChangeReceiver.unRegisterReceiver(this);
    }

    @Override
    public void onNetDisconnected() {
        Logger.i(TAG, "onNetDisconnected");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        Logger.i(TAG, networkType.toString());
    }
}
