package com.noe.rxjava.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by lijie on 2018/4/24.
 * 识别人脸
 */
public class FaceDetectorUtil {

    public static int detectionFaces(Bitmap bitmap, int maxFaces) {
        long start = System.currentTimeMillis();
        byte[] bytes = bitmap2Bytes(bitmap);
        bitmap.recycle();
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 2;
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;//一定是 565，其他识别不了。
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, mOptions);
        FaceDetector.Face[] faces = new FaceDetector.Face[maxFaces];
        FaceDetector faceDetector = new FaceDetector(bm.getWidth(), bm.getHeight(), maxFaces);
        int count = faceDetector.findFaces(bm, faces);
        bm.recycle();
        long end = System.currentTimeMillis();
        Log.i("xxxxxxxxxx", (end - start) + "");
        return count;
    }

    private static byte[] bitmap2Bytes(Bitmap bitmap) {
        long start = System.currentTimeMillis();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bs);
        long end = System.currentTimeMillis();
        Log.i("yyyyyyyy", (end - start) + "");
        return bs.toByteArray();
    }


    /**
     * @param bitmap   位图
     * @param maxFaces 一次可识别的最大数
     * @return 返回识别到的人脸数
     */
    public static int detectionFaces(Bitmap bitmap, int maxFaces, boolean needRecycle) {
        if (bitmap == null) {
            return 0;
        }
        byte[] data = bitmapToBytes(bitmap, needRecycle);
        if (data == null || data.length == 0) {
            return 0;
        }
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565; //必须设置为565，否则无法检测
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, mOptions);
        FaceDetector.Face[] faces = new FaceDetector.Face[maxFaces];
        FaceDetector faceDetector = new FaceDetector(bm.getWidth(), bm.getHeight(), maxFaces);
        int count = faceDetector.findFaces(bm, faces);
        bm.recycle();
        return count;
    }

    /**
     * 将 bitmap 转换成字节数组
     *
     * @param bitmap      要转换的 bitmap
     * @param needRecycle 转换后是否要回收 bitmap
     * @return 转换成功后的字节数组
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
//        StreamUtil.closeStream(output);
        return result;
    }
}
