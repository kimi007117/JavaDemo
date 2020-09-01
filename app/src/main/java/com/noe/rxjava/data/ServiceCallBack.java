package com.noe.rxjava.data;

/**
 * Created by 58 on 2016/7/15.
 */
public interface ServiceCallBack {
    void onResponse(Object result);
    void onFailure(Exception error);
}
