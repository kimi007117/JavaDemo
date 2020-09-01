package com.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.android.ui.R;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

/**
 * Created by lijie on 2020/8/21.
 */
public class GradientRecyclerView extends RecyclerView {
    private static final int MSG_SET_ALWAYS_SCROLL = 0x01;

    private Paint paint;
    private Matrix matrix;
    private LinearGradient shader;

    private static final float DEFAULT_GRADIENT_HEIGHT = 100f;
    private int gradientHeight;

    private ListViewState currentState;

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_ALWAYS_SCROLL:
                    currentState = ListViewState.AUTO_SCROLL_TO_BOTTOM;
                    break;
            }
        }
    };

    private RecyclerView.OnScrollListener onScrollListener;

    public GradientRecyclerView(Context context) {
        this(context, null);
    }

    public GradientRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientRecyclerView);
        gradientHeight = (int) a.getDimension(R.styleable.GradientRecyclerView_gradientHeight, DEFAULT_GRADIENT_HEIGHT);
        init();
        a.recycle();
    }

    public void setGradientHeight(int gradientHeight) {
        this.gradientHeight = gradientHeight;
    }

    private void init() {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        Message msg = mHandler.obtainMessage(MSG_SET_ALWAYS_SCROLL);
                        mHandler.sendMessageDelayed(msg, 2000);
                        break;
                    case SCROLL_STATE_DRAGGING:
                        mHandler.removeMessages(MSG_SET_ALWAYS_SCROLL);
                        currentState = ListViewState.STOP_AUTO_SCROLL;
                        break;
                }
            }
        };

        addOnScrollListener(onScrollListener);
        currentState = ListViewState.AUTO_SCROLL_TO_BOTTOM;
        paint = new Paint();
        matrix = new Matrix();
        shader = new LinearGradient(0, 0, 0, 1, 0xFF000000, 0, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public ListViewState getCurrentState() {
        return currentState;
    }

    public enum ListViewState {
        AUTO_SCROLL_TO_BOTTOM,
        STOP_AUTO_SCROLL
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, ALL_SAVE_FLAG);
        super.draw(canvas);
        matrix.setScale(1, gradientHeight * 1);
        matrix.postTranslate(0, 0);
        shader.setLocalMatrix(matrix);
        canvas.drawRect(0, 0, getWidth(), gradientHeight, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_SET_ALWAYS_SCROLL);
    }
}
