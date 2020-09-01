package com.noe.rxjava.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by lijie on 2018/4/19.
 * 利用系统提供的FaceDetector实现人脸识别
 */
public class SystemFaceDetector implements IFaceDetectorPresenter, Runnable {
    private static final String TAG = "SystemFaceDetector";
    private Thread mThread;
    private boolean mStopTrack;
    private FaceDataListener mDataListener;
    private FaceDetector.Face[] mFaces;
    private byte[] mPreviewBuffer;

    private DetectorData mDetectorData;
    private Camera mCamera;
    private int mCameraId;
    private float mZoomRatio;//缩放比例
    private int mCameraWidth;
    private int mCameraHeight;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private int mOrientationOfCamera;
    private int mMaxFacesCount;
    private boolean mOpenCamera = false;

    public SystemFaceDetector() {
        mDetectorData = new DetectorData();
    }

    @Override
    public void run() {
        mStopTrack = false;
        while (!mStopTrack) {
            if (!mOpenCamera) {
                continue;
            }
            detectionFaces();
            if (mDataListener != null) {
                mDataListener.onDetectorData(mDetectorData);
            }
        }
    }

    private void detectionFaces() {
        if (mCamera == null || mDetectorData.faceData == null || mDetectorData.faceData.length == 0) {
            return;
        }
        /*
          这里需要注意，回调出来的data不是我们直接意义上的RGB图 而是YUV图，因此我们需要
          将YUV转化为bitmap再进行相应的人脸检测，同时注意必须使用RGB_565，才能进行人脸检测，其余无效
         */
        try {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(mDetectorData.faceData, ImageFormat.NV21, size.width, size.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, baos);
            mPreviewBuffer = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mPreviewBuffer == null || mPreviewBuffer.length == 0) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;//必须设置为565，否则无法检测
        Bitmap bitmap = BitmapFactory.decodeByteArray(mPreviewBuffer, 0, mPreviewBuffer.length, options);
        if (bitmap == null) {
            return;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        //设置各个角度的相机，这样我们的检测效果才是最好
        switch (mOrientationOfCamera) {
            case 0:
                matrix.postRotate(0.0f, width / 2, height / 2);
                break;
            case 90:
                matrix.postRotate(-270.0f, height / 2, width / 2);
                break;
            case 180:
                matrix.postRotate(-180.0f, width / 2, height / 2);
                break;
            case 270:
                matrix.postRotate(-90.0f, height / 2, width / 2);
                break;
        }
        matrix.postScale(0.2f, 0.2f);//为了减小内存压力，将图片缩放，但是也不能太小，否则检测不到人脸
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        //初始化人脸检测
        FaceDetector detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), mMaxFacesCount);
        mFaces = new FaceDetector.Face[mMaxFacesCount];
        //这里通过向findFaces中传递帧图转化后的bitmap和最大检测的人脸数face，返回检测后的人脸数
        mDetectorData.facesCount = detector.findFaces(bitmap, mFaces);
        //绘制识别后的人脸区域的类
        getFaceRect();
        bitmap.recycle();
    }

    /**
     * 计算识别框
     */
    private void getFaceRect() {
        Rect[] faceRectList = new Rect[mDetectorData.facesCount];
        Rect rect = null;
        int index = 0;
        float distance = 0;
        for (int i = 0; i < mDetectorData.facesCount; i++) {
            faceRectList[i] = new Rect();
            FaceDetector.Face face = mFaces[i];
            if (face != null) {
                float eyeDistance = face.eyesDistance();
                eyeDistance = eyeDistance * mZoomRatio;
                if (eyeDistance > distance) {
                    distance = eyeDistance;
                    rect = faceRectList[i];
                    index = i;
                }
                PointF midEyesPoint = new PointF();
                face.getMidPoint(midEyesPoint);
                midEyesPoint.x = midEyesPoint.x * mZoomRatio;
                midEyesPoint.y = midEyesPoint.y * mZoomRatio;
                Log.i(TAG, "eyeDistance:" + eyeDistance + ",midEyesPoint.x:" + midEyesPoint.x
                        + ",midEyesPoint.y:" + midEyesPoint.y);
                faceRectList[i].set((int) (midEyesPoint.x - eyeDistance),
                        (int) (midEyesPoint.y - eyeDistance),
                        (int) (midEyesPoint.x + eyeDistance),
                        (int) (midEyesPoint.y + eyeDistance));
                Log.i(TAG, "FaceRectList[" + i + "]:" + faceRectList[i]);
            }
        }
        int width = (int) (mPreviewHeight * mZoomRatio / 5);
        if (rect != null && mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            int left = rect.left;
            rect.left = width - rect.right;
            rect.right = width - left;
            faceRectList[index].left = rect.left;
            faceRectList[index].right = rect.right;
        }
        mDetectorData.lightIntensity = FaceUtil.getYUVLight(mDetectorData.faceData, rect, width);
        mDetectorData.faceRectList = faceRectList;
        if (mCameraWidth > 0) {
            mDetectorData.distance = distance * 2.5f / mCameraWidth;
        }
    }

    /**
     * 开启识别
     */
    @Override
    public void detector() {
        mThread = new Thread(this);
        mThread.start();
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (mDetectorData != null) {
            mDetectorData.faceData = null;
        }
        mStopTrack = true;
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    /**
     * 设置检测监听
     */
    @Override
    public void setDataListener(FaceDataListener mDataListener) {
        this.mDataListener = mDataListener;
    }

    /**
     * 设置预览数据
     */
    @Override
    public void setCameraPreviewData(byte[] data, Camera camera) {
        if (mDetectorData != null) {
            mDetectorData.faceData = data;
        }
        mCamera = camera;
    }

    /**
     * 设置识别最大人脸数量
     */
    @Override
    public void setMaxFacesCount(int mMaxFacesCount) {
        this.mMaxFacesCount = mMaxFacesCount;
    }

    /**
     * 设置相机高度
     *
     * @param mCameraHeight 相机高度
     */
    @Override
    public void setCameraHeight(int mCameraHeight) {
        this.mCameraHeight = mCameraHeight;
    }

    /**
     * 设置相机宽度
     *
     * @param mCameraWidth 相机宽度
     */
    @Override
    public void setCameraWidth(int mCameraWidth) {
        this.mCameraWidth = mCameraWidth;
    }

    /**
     * 设置相机方向
     *
     * @param mOrientationOfCamera 相机方向
     */
    @Override
    public void setOrientationOfCamera(int mOrientationOfCamera) {
        this.mOrientationOfCamera = mOrientationOfCamera;
        Log.i("ssssssssssssssssssss",mOrientationOfCamera+"");
    }

    /**
     * 设置缩放比例
     */
    @Override
    public void setZoomRatio(float mZoomRatio) {
        this.mZoomRatio = mZoomRatio;
    }

    /**
     * 设置相机是否打开
     */
    @Override
    public void setOpenCamera(boolean isOpenCamera) {
        this.mOpenCamera = isOpenCamera;
    }

    /**
     * 设置相机ID
     */
    @Override
    public void setCameraId(int mCameraId) {
        this.mCameraId = mCameraId;
    }

    /**
     * 设置预览高度
     */
    @Override
    public void setPreviewHeight(int mPreviewHeight) {
        this.mPreviewHeight = mPreviewHeight;
    }

    /**
     * 设置预览宽度
     */
    @Override
    public void setPreviewWidth(int mPreviewWidth) {
        this.mPreviewWidth = mPreviewWidth;
    }
}
