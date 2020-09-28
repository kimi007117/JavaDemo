package com.noe.rxjava;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.noe.rxjava.util.ArouterUtils;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

@Route(path = ArouterUtils.ACTIVITY_VIEWFLIPPER)
public class ViewFlipperActivity extends AppCompatActivity {
    private GifImageView mGifImageView;
    //http://cdn.duitang.com/uploads/item/201306/14/20130614163948_SvNm8.gif
    //http://imgsrc.baidu.com/forum/w%3D580/sign=a4bda67898504fc2a25fb00dd5dce7f0/94f8a7efce1b9d167669b011f0deb48f8d5464d6.jpg
    //http://img4.duitang.com/uploads/item/201412/30/20141230232522_ERPiv.gif
    //http://img.mp.itc.cn/upload/20160819/70048ed133e04d55a58c58a557b9178c.jpg
    private String url = "http://imgsrc.baidu.com/forum/w%3D580/sign=a4bda67898504fc2a25fb00dd5dce7f0/94f8a7efce1b9d167669b011f0deb48f8d5464d6.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);
        findViewById(R.id.btn_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ArouterUtils.ACTIVITY_DOUBAN).navigation();
            }
        });
        mGifImageView = (GifImageView) findViewById(R.id.iv_gif);
        Glide.with(this).load(url).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                try {
                    GifDrawable gifDrawable = new GifDrawable(resource);
                    mGifImageView.setImageDrawable(gifDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.iv_test);
        imageView.setImageResource(0);
//        Glide.with(this).load(url).into(mGifImageView);
//        ImageLoaderManager.getInstance().showImage(ImageLoaderManager.getDefaultOptions(mGifImageView,url));
    }
}
