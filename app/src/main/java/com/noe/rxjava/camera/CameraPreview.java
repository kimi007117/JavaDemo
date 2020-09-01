package com.noe.rxjava.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by lijie on 2018/4/19.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mCameraId;
    private IFaceDetectorPresenter mFaceDetector;
    private int mCameraWidth;
    private int mCameraHeight;

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mHolder = getHolder();
        mHolder.addCallback(this);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mCameraWidth = metrics.widthPixels;
        mCameraHeight = metrics.heightPixels;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }


    /**
     * 打开相机
     */
    public void openCamera() {
        if (null != mCamera) {
            return;
        }

        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        if (mFaceDetector != null) {
            mFaceDetector.setCameraId(mCameraId);
        }

        try {
            mCamera = Camera.open(mCameraId);
            // setParameters 针对部分手机通过Camera.open()拿到的Camera对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
            if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                Log.i(TAG, "前置摄像头已开启");
            } else {
                Log.i(TAG, "后置摄像头已开启");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //关闭相机
            closeCamera();
            return;
        }

        if (mCamera == null || mHolder == null || mHolder.getSurface() == null) {
            return;
        }

        try {

            //设置预览回调
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (mFaceDetector != null) {
                        mFaceDetector.setCameraPreviewData(data, camera);
                        mFaceDetector.setOpenCamera(true);
                    }
                }
            });
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mCamera.setPreviewDisplay(mHolder);
            Log.i(TAG, "camera size width:" + mCameraWidth + ",height:" + mCameraHeight);
            if (mFaceDetector != null) {
                mFaceDetector.setCameraWidth(mCameraWidth);
                mFaceDetector.setCameraHeight(mCameraHeight);
            }
            //设置相机参数
            FaceUtil.setCameraParams(this, mFaceDetector, mCamera, mCameraId, mCameraWidth, mCameraHeight);
            //开始预览
            mCamera.startPreview();
        } catch (Exception e) {
            closeCamera();
            Log.i(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera() {
        if (mFaceDetector != null) {
            mFaceDetector.setOpenCamera(false);
        }
        if (null != mCamera) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        closeCamera();
        if (mFaceDetector != null) {
            mFaceDetector.release();
        }
    }

    public Camera getCamera() {
        return mCamera;
    }

    public CameraPreview setFaceDetector(IFaceDetectorPresenter mFaceDetector) {
        this.mFaceDetector = mFaceDetector;
        return this;
    }

    public int getCameraId() {
        return mCameraId;
    }

    public CameraPreview setCameraId(int mCameraId) {
        this.mCameraId = mCameraId;
        return this;
    }

}
