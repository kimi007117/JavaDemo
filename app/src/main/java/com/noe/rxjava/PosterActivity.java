package com.noe.rxjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.noe.rxjava.base.BaseActivity;
import com.noe.rxjava.util.ArouterUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lijie on 2019-09-23.
 */
@Route(path = ArouterUtils.ACTIVITY_POSTER)
public class PosterActivity extends BaseActivity {

    private Button mBtnShare;
    private LinearLayout rootView;

    private Bitmap mPosterBitmap;
    private String mShareImgUrl;

    private final String POSTER_IMG_NAME = "SalesUserPoster.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        mBtnShare = findViewById(R.id.btn_share);
        rootView = findViewById(R.id.layout);
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap();
            }
        });
    }

    private void saveBitmap() {
        if (mPosterBitmap == null) {
            rootView.setDrawingCacheEnabled(true);
            rootView.buildDrawingCache();
            mPosterBitmap = rootView.getDrawingCache();
        }
        // cache方法无法获取bitmap 尝试用新的方式解决
        if (mPosterBitmap == null) {
            mPosterBitmap = getViewBitmap(rootView);
        }
        saveBitmap(mPosterBitmap, System.currentTimeMillis() + "");
    }

    /**
     * 获取 view 的bitmap 但是效率低 作为替补方案
     *
     * @param view
     * @return
     */
    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Bitmap bitmap, String bitName) {
        if (bitmap == null) {
            Toast.makeText(mContext, "设备暂不支持该功能", Toast.LENGTH_SHORT).show();
            mShareImgUrl = "";
            return;
        }

        String fileName;
        File file;
        // 小米手机
        if (Build.BRAND.equals("Xiaomi")) {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        mShareImgUrl = fileName;
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        try (FileOutputStream out = new FileOutputStream(file)) {

            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            //if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
            //    out.flush();
            //    out.close();
            //    // 插入图库
            //    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), bitName, null);
            //}
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            try {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    // 插入图库
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), bitName, null);
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
            // 发送广播，通知刷新图库的显示
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
