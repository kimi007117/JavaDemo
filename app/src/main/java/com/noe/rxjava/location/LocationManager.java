package com.noe.rxjava.location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.noe.rxjava.base.RxApplication;

/**
 * 百度地图定位
 */
public class LocationManager {

    private LocationClient mLocationClient;
    private OnLocationListener mOnLocationListener;

    private LocationManager() {
        init();
    }

    private static class LocationHolder {

        private static LocationManager INSTANCE = new LocationManager();
    }

    public static LocationManager getInstance() {
        return LocationHolder.INSTANCE;
    }

    private void init() {
        //定位初始化
        mLocationClient = new LocationClient(RxApplication.getInstance().getApplicationContext());

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
    }

    /**
     * 设置监听
     */
    public void setOnLocationListener(OnLocationListener listener) {
        mOnLocationListener = listener;
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.requestLocation();
            } else {
                mLocationClient.start();
            }
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    /**
     * 监听百度地图定位
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null) {
                if (mOnLocationListener != null) {
                    mOnLocationListener.onFailure();
                }
                return;
            }
            LocationInfo info = new LocationInfo();
            info.latitude = location.getLatitude();
            info.longitude = location.getLongitude();
            mOnLocationListener.onSuccess(info);
        }
    }
}
