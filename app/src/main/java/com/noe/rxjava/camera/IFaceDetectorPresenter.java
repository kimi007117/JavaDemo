package com.noe.rxjava.camera;

import android.hardware.Camera;

/**
 * Created by lijie on 2018/4/19.
 * 识别定义的接口
 */
public interface IFaceDetectorPresenter {
    void detector();

    void release();

    void setDataListener(FaceDataListener dataListener);

    void setCameraPreviewData(byte[] data, Camera camera);

    void setMaxFacesCount(int maxFacesCount);

    void setCameraHeight(int cameraHeight);

    void setCameraWidth(int cameraWidth);

    void setPreviewHeight(int previewHeight);

    void setPreviewWidth(int previewWidth);

    void setCameraId(int cameraId);

    void setOrientationOfCamera(int orientationOfCamera);

    void setZoomRatio(float zoomRatio);

    void setOpenCamera(boolean isOpenCamera);
}
