package com.noe.rxjava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.noe.rxjava.dialog.IMNotToast;
import com.noe.rxjava.image.CircleWithBorderTransformation;
import com.noe.rxjava.image.CircleWithTwoBorderTransformation;
import com.noe.rxjava.image.RoundCornersTransformation;
import com.noe.rxjava.util.ArouterUtils;
import com.noe.rxjava.util.FaceDetectorUtil;
import com.android.core.util.ScreenUtils;

@Route(path = ArouterUtils.ACTIVITY_GLIDE)
public class GlideActivity extends AppCompatActivity {
    private Activity mActivity;
    private ImageView mImageView;
    private ImageView mHeaderImageView;
    private ImageView mOneImageView;
    private static final String url = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3014402647,3821196097&fm=58";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        mActivity = this;
        mImageView = findViewById(R.id.iv_glide);
        mHeaderImageView = findViewById(R.id.iv_header);
        mOneImageView = findViewById(R.id.iv_header_one);
        MultiTransformation multi = new MultiTransformation(
                new CenterCrop(GlideActivity.this),
                new RoundCornersTransformation(GlideActivity.this, 12, 0));
        Glide.with(this)
                .load(url)
                .into(mImageView);

        Glide.with(this)
                .load(url)
                .transform(new CircleWithTwoBorderTransformation(this, ScreenUtils.dipToPixel(this, 3),
                        Color.parseColor("#fac400"), ScreenUtils.dipToPixel(this, 1), Color.parseColor("#d81619")))
                .into(mHeaderImageView);

        Glide.with(this)
                .load(url)
                .transform(new CircleWithBorderTransformation(this, ScreenUtils.dipToPixel(this, 5),
                        Color.parseColor("#fac400")))
                .into(mOneImageView);


        findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.banner);
                Toast.makeText(mActivity, FaceDetectorUtil.detectionFaces(bitmap, 1) + "", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SweetTopAlter.create(GlideActivity.this).setTitle("Alert Title")
//                        .setText("Alert text...").show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        WindowUtils.showPopupWindow(GlideActivity.this);
//                    }
//                },1000);
//                Toast toast = new Toast(GlideActivity.this);
//                toast.setDuration(Toast.LENGTH_LONG);
//                ViewGroup view = (ViewGroup) LayoutInflater.from(GlideActivity.this).inflate(R.layout.alerter_alert_view, null);
//                toast.setView(view);
//                toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL,0,0);
//                toast.show();
                IMNotToast.makeText(GlideActivity.this, "哈哈哈哈哈哈", "呵呵呵呵", IMNotToast.INTEGRAL_SYSTEM_TYPE).show();
//                FloatWindowManager.getInstance().applyOrShowFloatWindow(GlideActivity.this);
            }
        });
    }
}
