package com.noe.rxjava.network;

import android.support.annotation.NonNull;

/**
 * Created by lijie on 2020/7/28.
 */
public enum NetworkType {
    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_2G("2G"),
    NETWORK_3G("3G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    private String desc;
    NetworkType(String desc) {
        this.desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return desc;
    }
}
