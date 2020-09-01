package com.noe.rxjava.location;

/**
 * 百度地图定位监听
 */
public interface OnLocationListener {

    /**
     * 定位成功
     */
    void onSuccess(LocationInfo locationInfo);

    /**
     * 定位成功
     */
    void onFailure();
}
