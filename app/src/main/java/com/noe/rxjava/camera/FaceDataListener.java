package com.noe.rxjava.camera;

/**
 * Created by lijie on 2018/4/19.
 * 识别数据监听
 */
public interface FaceDataListener {
    void onDetectorData(DetectorData detectorData);
}
