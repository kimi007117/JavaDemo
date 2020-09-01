package com.noe.rxjava.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.noe.rxjava.R;


/**
 * Created by lijie on 2017/5/5.
 */
public class XRecyclerViewFooter extends LinearLayout {
    static final int ROTATION_ANIMATION_DURATION = 1000;
    public final static int STATE_LOADING = 0; //正在加载
    public final static int STATE_COMPLETE = 1;  //加载完成
    private ImageView mProgressBar;    // 正在刷新的图标
    // 均匀旋转动画
    private Animation mRotateAnimation;
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    public XRecyclerViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XRecyclerViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XRecyclerViewFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 初始化
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.listview_footer, this);
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(lp);
        mProgressBar = (ImageView) findViewById(R.id.listview_foot_progress);
        // 添加匀速转动动画
        mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                this.setVisibility(VISIBLE);
                mProgressBar.startAnimation(mRotateAnimation);
                break;
            case STATE_COMPLETE:
                mProgressBar.clearAnimation();
                this.setVisibility(GONE);
                break;
            default:
                break;
        }
    }
}
