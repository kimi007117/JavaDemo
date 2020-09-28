package com.noe.rxjava;

import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.core.util.Logger;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.location.LocationInfo;
import com.noe.rxjava.location.LocationManager;
import com.noe.rxjava.location.OnLocationListener;
import com.noe.rxjava.util.ArouterUtils;
import com.android.core.util.ScreenUtils;
import com.noe.rxjava.widget.TestBottomSheetLayout;

/**
 * 百度地图
 * Created by lijie on 2020-03-20.
 */
@Route(path = ArouterUtils.ACTIVITY_MAP)
public class MapActivity extends BaseActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mBtnSearch;
    private EditText mEtKeyword;
    private TextView mBtnCheckMore;

    TestBottomSheetLayout sheetLayout;

    private boolean isFirst = true;
    private boolean stopLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initListener();
        startLocation();
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
        mBtnCheckMore = findViewById(R.id.btn_check_more);
        mBtnSearch = findViewById(R.id.btn_search);
        mEtKeyword = findViewById(R.id.et_keyword);
        sheetLayout = findViewById(R.id.view_sheet);
        initMap();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mBtnCheckMore.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mEtKeyword.setOnEditorActionListener(mEditorActionListener);
    }


    /**
     * 初始化地图设置
     */
    private void initMap() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);

        mBaiduMap = mMapView.getMap();
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setRotateGesturesEnabled(false);//屏蔽旋转
        settings.setCompassEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);
        //设置缩放级别，默认级别为16
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.setMapStatus(mapstatusUpdate);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false,
                BitmapDescriptorFactory.fromResource(R.drawable.icon_geo)));
        //设置地图状态监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        //设置触摸地图事件监听者
        mBaiduMap.setOnMapTouchListener(mOnMapTouchListener);
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        LocationManager.getInstance().setOnLocationListener(mOnLocationListener);
        LocationManager.getInstance().startLocation();
        addMarker();
    }

    /**
     * 定位监听
     */
    private OnLocationListener mOnLocationListener = new OnLocationListener() {
        @Override
        public void onSuccess(LocationInfo locationInfo) {
            if (locationInfo == null || mMapView == null || stopLocation) {
                return;
            }
            LatLng latLng = new LatLng(locationInfo.latitude, locationInfo.longitude);

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng);
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(builder.build());

            MyLocationData locData = new MyLocationData.Builder()
                    .latitude(locationInfo.latitude)
                    .longitude(locationInfo.longitude).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirst) {
                mBaiduMap.animateMapStatus(update);
                isFirst = false;
            }
            Logger.i("Location LatLng", "latitude:" + locationInfo.latitude + "longitude:" + locationInfo.longitude);
        }

        @Override
        public void onFailure() {

        }
    };

    private void addMarker() {
        //定义Maker坐标点
        LatLng point = new LatLng(39.963175, 116.400244);
//        //构建Marker图标
        View view = View.inflate(this, R.layout.layout_map_marker, null);
        //将View转化为Bitmap
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(view);

//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .perspective(true)
                .visible(true)
                .icon(descriptor);
//        Button button = new Button(getActivity());
//        button.setBackgroundResource(R.drawable.bg_marker_info);
//        button.setText("InfoWindow");
//
////响应点击的OnInfoWindowClickListener
//        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick() {
//                Toaster.showToast("Click on InfoWindow");
//            }
//        };
//
////构造InfoWindow
////point 描述的位置点
////-100 InfoWindow相对于point在y轴的偏移量
//        InfoWindow mInfoWindow = new InfoWindow(button, point, -100);
////使InfoWindow生效
//        mBaiduMap.showInfoWindow(mInfoWindow);
//
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.btn_check_more) {
            // 查看更多
            Toast.makeText(mContext, "查看更多", Toast.LENGTH_SHORT).show();
            showBottomSheet();
        } else if (id == R.id.btn_search) {
            // 搜索
            doSearch();
        }

    }

    /**
     * 地图状态改变
     */
    private BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * @param mapStatus 地图状态改变开始时的地图状态
         */
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        /** 因某种操作导致地图状态开始改变。
         * @param mapStatus 地图状态改变开始时的地图状态
         * @param reason 表示地图状态改变的原因，取值有：
         * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
         * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
         * 3：开发者调用,导致的地图状态改变
         */
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int reason) {

        }

        /**
         * 地图状态变化中
         * @param mapStatus 当前地图状态
         */
        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        /**
         * 地图状态改变结束
         * @param mapStatus 地图状态改变结束后的地图状态
         */
        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng target = mapStatus.target;
            Logger.i("OnMapStatusChangeListener LatLng", "latitude:" + target.latitude + "longitude:" + target.longitude);
        }
    };

    /**
     * 触摸地图回调
     */
    BaiduMap.OnMapTouchListener mOnMapTouchListener = new BaiduMap.OnMapTouchListener() {

        /**
         * 当用户触摸地图时回调函数
         *
         * @param motionEvent 触摸事件
         */
        @Override
        public void onTouch(MotionEvent motionEvent) {
            hideIMSoftKeyboard();
            hideBottomSheet();
        }
    };

    BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        //marker被点击时回调的方法
        //若响应点击事件，返回true，否则返回false
        //默认返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Point pt = new Point();
            pt.x = ScreenUtils.getScreenWidth(mContext) / 2;
            pt.y = (ScreenUtils.getScreenHeight(mContext) - ScreenUtils.dipToPixel(mContext, 500)) / 2;
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.targetScreen(pt);
            MapStatus build = builder.build();

            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(build));
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
            mBaiduMap.animateMapStatus(update);

            showBottomSheet();
            return true;
        }
    };

    /**
     * 软键盘搜索键监听
     */
    private TextView.OnEditorActionListener mEditorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            doSearch();
            return true;
        }
        return false;
    };

    /**
     * 执行搜索
     */
    private void doSearch() {
        String keyword = mEtKeyword.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(mContext, "请输入您的关键词", Toast.LENGTH_SHORT).show();
            return;
        }
        hideIMSoftKeyboard();
        Toast.makeText(mContext, "执行搜索", Toast.LENGTH_SHORT).show();
    }

    private void showBottomSheet() {
        sheetLayout.show();
    }

    private void hideBottomSheet() {
        sheetLayout.hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        LocationManager.getInstance().stopLocation();
    }
}
