package com.noe.rxjava;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.camera.CameraPreview;
import com.noe.rxjava.camera.DetectorData;
import com.noe.rxjava.camera.DetectorProxy;
import com.noe.rxjava.camera.FaceDataListener;
import com.noe.rxjava.camera.FaceRectView;
import com.noe.rxjava.util.ArouterUtils;

import java.io.File;
import java.util.Calendar;

@Route(path = ArouterUtils.ACTIVITY_FACE_DETECTOR)
public class FaceDetectorActivity extends AppCompatActivity {
    private static final String TAG = "FaceDetectorActivity";
    private CameraPreview mFace_detector_preview;
    private FaceRectView mFace_detector_face;
    private Button mFace_detector_take_photo;
    private TextView mFace_detector_distance;

    private Context mContext;
    private DetectorProxy mDetectorProxy;
    private DetectorData mDetectorData;
    private MediaRecorder mRecorder;
    private Button mButtonChange;

    private boolean isVideo = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mDetectorData != null) {
                mFace_detector_distance.setText(String.valueOf(mDetectorData.lightIntensity));
            }
        }
    };


    private FaceDataListener mDataListener = new FaceDataListener() {
        @Override
        public void onDetectorData(DetectorData detectorData) {
            mDetectorData = detectorData;
            Log.i(TAG, "识别数据:" + detectorData);
            mHandler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detector);
        mContext = this;
        init();
    }

    protected void init() {
        mFace_detector_preview = (CameraPreview) findViewById(R.id.face_detector_preview);
        mFace_detector_face = (FaceRectView) findViewById(R.id.face_detector_face);
        mFace_detector_face.setZOrderOnTop(true);
        mFace_detector_face.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mFace_detector_take_photo = (Button) findViewById(R.id.face_detector_take_photo);
        mFace_detector_distance = (TextView) findViewById(R.id.face_detector_distance);
        mButtonChange = (Button) findViewById(R.id.btn_change);
        // 点击SurfaceView，切换摄相头
        mButtonChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 只有一个摄相头，不支持切换
                if (Camera.getNumberOfCameras() == 1) {
                    return;
                }
                if (mDetectorProxy == null) {
                    return;
                }
                mDetectorProxy.closeCamera();
                if (Camera.CameraInfo.CAMERA_FACING_FRONT == mDetectorProxy.getCameraId()) {
                    mDetectorProxy.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
                } else {
                    mDetectorProxy.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }
                mDetectorProxy.openCamera();


            }
        });

        mFace_detector_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isVideo) {
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder();
                    }
                    isVideo = true;
                    mFace_detector_preview.getCamera().unlock();
                    mRecorder.setCamera(mFace_detector_preview.getCamera());

                    try {
                        // 这两项需要放在setOutputFormat之前
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                        // Set output file format
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                        // 这两项需要放在setOutputFormat之后
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

//                        mRecorder.setVideoSize(640, 480);
//                        mRecorder.setVideoFrameRate(30);
//                        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
                        mRecorder.setOrientationHint(90);
                        //设置记录会话的最大持续时间（毫秒）
                        mRecorder.setMaxDuration(30 * 1000);
                        mRecorder.setPreviewDisplay(mFace_detector_preview.getHolder().getSurface());
                        String path = getSDPath();
                        File dir = new File(path + "/recordtest");
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        path = dir + "/" + getDate() + ".mp4";
                        mRecorder.setOutputFile(path);
                        mRecorder.prepare();
                        mRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        isVideo = false;
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mRecorder = null;
                        if (mFace_detector_preview.getCamera() != null) {
                            mFace_detector_preview.getCamera().release();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mDetectorProxy = new DetectorProxy.Builder(mFace_detector_preview)
                .setDataListener(mDataListener)
                .setFaceRectView(mFace_detector_face)
                .setDrawFaceRect(true)
                .build();
    }

    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        return "" + year + (month + 1) + day + hour + minute + second;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDetectorProxy != null) {
            mDetectorProxy.detector();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDetectorProxy != null) {
            mDetectorProxy.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }


    private String getDiskCacheDir(Context context, String dirName) {
        String cachePath = "";
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && context != null && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            if (context != null && context.getCacheDir() != null) {
                cachePath = context.getCacheDir().getPath();
            }
        }
        return cachePath + File.separator + dirName;
    }

    private File getOutputFile(Context context, String dirName, String fileName) {
        File dirFile = new File(getDiskCacheDir(context, dirName));
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        File file = new File(dirFile.getPath() + File.separator + fileName);
        return file;
    }


}
