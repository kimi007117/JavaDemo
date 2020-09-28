package com.noe.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 图片合成
 * Created by lijie on 2019-10-10.
 */
@Route(path = ArouterUtils.ACTIVITY_COMPOSE)
public class ComposeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merge();
            }
        });
    }

    private void merge() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String compose = compose();
                subscriber.onNext(compose);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("xxxxxxxxxxx3", Thread.currentThread() + "");
                        Log.i("xxxxxxxxxxx4", (Looper.getMainLooper() == Looper.myLooper()) + "");
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String compose() {
        File zhang = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "1569209064069.jpg");
        File phil = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "55.png");

        try {
            Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(zhang));
            Bitmap bitmap2 = BitmapFactory.decodeStream(new FileInputStream(phil));

            Bitmap newBmp = newBitmap(bitmap1, bitmap2);

            File zhangphil = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "zhangphil.png");
            if (!zhangphil.exists()) {
                zhangphil.createNewFile();
            }
            save(newBmp, zhangphil, Bitmap.CompressFormat.JPEG, true);
            Log.i("xxxxxxxxxxx1", Thread.currentThread() + "");
            Log.i("xxxxxxxxxxx2", (Looper.getMainLooper() == Looper.myLooper()) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "合成完成";
    }

    private Bitmap newBitmap(Bitmap bmp1, Bitmap bmp2) {
        Bitmap retBmp;
        int width = bmp1.getWidth();
        if (bmp2.getWidth() != width) {
            //以第一张图片的宽度为标准，对第二张图片进行缩放。

            int h2 = bmp2.getHeight() * width / bmp2.getWidth();
            retBmp = Bitmap.createBitmap(width, bmp1.getHeight() + h2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(retBmp);
            Bitmap newSizeBmp2 = resizeBitmap(bmp2, width, h2);
            canvas.drawBitmap(bmp1, 0, 0, null);
            canvas.drawBitmap(newSizeBmp2, 0, bmp1.getHeight(), null);
        } else {
            //两张图片宽度相等，则直接拼接。

            retBmp = Bitmap.createBitmap(width, bmp1.getHeight() + bmp2.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(retBmp);
            canvas.drawBitmap(bmp1, 0, 0, null);
            canvas.drawBitmap(bmp2, 0, bmp1.getHeight(), null);
        }

        return retBmp;
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    private boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return false;
        }

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), null);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    private boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


}
