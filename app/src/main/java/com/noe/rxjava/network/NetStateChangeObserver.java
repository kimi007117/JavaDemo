package com.noe.rxjava.network;


/**
 * Created by lijie on 2020/7/28.
 */
public interface NetStateChangeObserver {
    void onNetDisconnected();
    void onNetConnected(NetworkType networkType);
}
