package com.noe.rxjava.camera;

import android.graphics.Color;
import android.hardware.Camera;


/**
 * Created by lijie on 2018/4/19.
 * 识别代理类
 */
public class DetectorProxy {
    private CameraPreview mCameraPreview;
    private FaceRectView mFaceRectView;
    private IFaceDetectorPresenter mFaceDetector;
    private boolean mDrawFaceRect;

    /**
     * 构造函数，需传入自定义相机预览界面
     *
     * @param mCameraPreview 相机预览界面
     */
    private DetectorProxy(CameraPreview mCameraPreview) {
        this.mCameraPreview = mCameraPreview;
    }

    /**
     * 设置绘制人脸检测框界面
     */
    public void setFaceRectView(FaceRectView mFaceRectView) {
        this.mFaceRectView = mFaceRectView;
    }

    /**
     * 设置人脸检测类，默认实现为原生检测类，可以替换成第三方库检测类
     *
     * @param faceDetector 人脸检测类
     */
    public void setFaceDetector(IFaceDetectorPresenter faceDetector) {
        if (faceDetector != null) {
            this.mFaceDetector = faceDetector;
        }
        if (mCameraPreview != null) {
            mCameraPreview.setFaceDetector(this.mFaceDetector);
        }
    }

    /**
     * 设置检测监听
     */
    public void setDataListener(final FaceDataListener mDataListener) {
        if (mFaceDetector != null) {
            mFaceDetector.setDataListener(new FaceDataListener() {
                @Override
                public void onDetectorData(DetectorData detectorData) {
                    if (mDrawFaceRect && mFaceRectView != null && detectorData != null
                            && detectorData.faceRectList != null) {
                        mFaceRectView.drawFaceRect(detectorData);
                    }
                    if (mDataListener != null) {
                        mDataListener.onDetectorData(detectorData);
                    }
                }
            });
        }
    }

    /**
     * 设置相机预览为前置还是后置摄像头
     */
    public void setCameraId(int mCameraId) {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK
                || mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (mCameraPreview != null) {
                mCameraPreview.setCameraId(mCameraId);
            }
        }
    }

    /**
     * 设置检测最大人脸数量
     */
    public void setMaxFacesCount(int mMaxFacesCount) {
        if (mFaceDetector != null) {
            mFaceDetector.setMaxFacesCount(mMaxFacesCount);
        }
    }

    /**
     * 设置是否绘制人脸检测框
     */
    public void setDrawFaceRect(boolean mDrawFaceRect) {
        this.mDrawFaceRect = mDrawFaceRect;
    }

    /**
     * 设置人脸检测框是否是矩形
     */
    public void setFaceIsRect(boolean mFaceIsRect) {
        if (mFaceRectView != null) {
            mFaceRectView.setFaceIsRect(mFaceIsRect);
        }
    }

    /**
     * 设置人脸检测框颜色
     */
    public void setFaceRectColor(int rectColor) {
        if (mFaceRectView != null) {
            mFaceRectView.setRectColor(rectColor);
        }
    }

    /**
     * 开启检测
     */
    public void detector() {
        if (mFaceDetector != null) {
            mFaceDetector.detector();
        }
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        if (mCameraPreview != null) {
            mCameraPreview.openCamera();
        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera() {
        if (mCameraPreview != null) {
            mCameraPreview.closeCamera();
        }
    }

    /**
     * 获取相机ID
     */
    public int getCameraId() {
        if (mCameraPreview != null) {
            return mCameraPreview.getCameraId();
        }
        return Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mCameraPreview != null) {
            mCameraPreview.release();
        }
    }

    public static class Builder {
        private static final int MAX_DETECTOR_FACES = 5;

        private CameraPreview mCameraPreview;
        private FaceRectView mFaceRectView;
        private FaceDataListener mDataListener;
        private IFaceDetectorPresenter mFaceDetector = new SystemFaceDetector();
        private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        private int mMaxFacesCount = MAX_DETECTOR_FACES;
        private int mFaceRectColor = Color.rgb(255, 203, 15);
        private boolean mDrawFaceRect = false;
        private boolean mFaceIsRect = false;

        public Builder(CameraPreview mCameraPreview) {
            this.mCameraPreview = mCameraPreview;
        }

        public Builder setFaceRectView(FaceRectView mFaceRectView) {
            this.mFaceRectView = mFaceRectView;
            return this;
        }

        public Builder setFaceDetector(IFaceDetectorPresenter mFaceDetector) {
            this.mFaceDetector = mFaceDetector;
            return this;
        }

        public Builder setCameraId(int mCameraId) {
            this.mCameraId = mCameraId;
            return this;
        }

        public Builder setDataListener(FaceDataListener mDataListener) {
            this.mDataListener = mDataListener;
            return this;
        }

        public Builder setMaxFacesCount(int mMaxFacesCount) {
            this.mMaxFacesCount = mMaxFacesCount;
            return this;
        }

        public Builder setDrawFaceRect(boolean mDrawFaceRect) {
            this.mDrawFaceRect = mDrawFaceRect;
            return this;
        }

        public Builder setFaceIsRect(boolean mFaceIsRect) {
            this.mFaceIsRect = mFaceIsRect;
            return this;
        }

        public Builder setFaceRectColor(int mFaceRectColor) {
            this.mFaceRectColor = mFaceRectColor;
            return this;
        }

        public DetectorProxy build() {
            DetectorProxy detectorProxy = new DetectorProxy(mCameraPreview);
            detectorProxy.setFaceDetector(mFaceDetector);
            detectorProxy.setDataListener(mDataListener);
            detectorProxy.setMaxFacesCount(mMaxFacesCount);
            if (mFaceRectView != null && mDrawFaceRect) {
                detectorProxy.setFaceRectView(mFaceRectView);
                detectorProxy.setDrawFaceRect(mDrawFaceRect);
                detectorProxy.setFaceRectColor(mFaceRectColor);
                detectorProxy.setFaceIsRect(mFaceIsRect);
            }
            detectorProxy.setCameraId(mCameraId);
            return detectorProxy;
        }
    }
}
