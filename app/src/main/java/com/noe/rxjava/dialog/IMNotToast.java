package com.noe.rxjava.dialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.IntDef;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.noe.rxjava.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * Description:
 * 1、通知(Notification)类型的Toast
 * 2、支持优选队列
 * 3、自定义通知icon
 */
public class IMNotToast {
    public final static Handler mHandler = new Myhandler();
    public static final int DEFAULT_DURATION = 3000;

    private static final int ADD_TOAST = 0x1000;
    private static final int SHOW_TOAST = 0x1001;
    private static final int HIDE_TOAST = 0x1002;

    //系统弹窗
    public static final int INTEGRAL_SYSTEM_TYPE = 0x1100;

    private int clickable = 1;

    private OnToastClickListener mOnToastClickListener;

    private TN tn;

    public static Queue<TN> priorityQueue = new PriorityQueue(7, new Comparator<TN>() {

        @Override
        public int compare(TN c1, TN c2) {
            return c2.getPriority() - c1.getPriority();
        }
    });


    public IMNotToast setToastClickListener(OnToastClickListener listener) {
        mOnToastClickListener = listener;
        return this;
    }

    private OnToastClickListener getToastClickListener() {
        return mOnToastClickListener;
    }

    public interface OnToastClickListener {
        public void onClick(View v);
    }

    private OnCloseToastListener mOnCloseToastListener;

    public OnCloseToastListener getOnCloseToastListener() {
        return mOnCloseToastListener;
    }

    public IMNotToast setOnCloseToastListener(OnCloseToastListener mOnCloseToastListener) {
        this.mOnCloseToastListener = mOnCloseToastListener;
        return this;
    }

    public interface OnCloseToastListener {
        void onClose(View v);
    }

    private static class Myhandler extends Handler {
        public Myhandler() {
        }

        public Myhandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TN toast;
            switch (msg.what) {
                case ADD_TOAST:
                    toast = (TN) msg.obj;
                    if (toast != null) {
                        priorityQueue.add(toast);
                        if (priorityQueue.size() == 1) {
                            sendEmptyMessage(SHOW_TOAST);
                        }
                    }
                    break;
                case SHOW_TOAST:
                    toast = priorityQueue.peek();
                    if (toast != null) {
                        toast.handleShow();
                    }
                    break;
                case HIDE_TOAST:
                    toast = priorityQueue.poll();
                    if (toast != null) {
                        toast.handleHide();
                    }
                    break;
            }
        }
    }


    public static IMNotToast makeText(Context context, CharSequence title, CharSequence text, @TypeInt int type) {
        return makeText(context, title, text, DEFAULT_DURATION, type);
    }

    public static IMNotToast makeText(Context context, CharSequence title, CharSequence text, int duration, @TypeInt int type) {
        final IMNotToast result = new IMNotToast();
        if (context == null) {
            return result;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.alerter_alert_view, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.hide();
                }
            });
//            TextView titleTv = (TextView) view.findViewById(R.id.top_title);
//            titleTv.setText(title);
//            TextView textView = (TextView) view.findViewById(R.id.text1);
//            textView.setText(text);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (result.clickable > 0) {
//                        result.clickable--;
//                        OnToastClickListener listener = result.getToastClickListener();
//                        if (listener != null) {
//                            listener.onClick(v);
//                        }
//                    }
//                }
//            });
//            ImageView close = (ImageView)view.findViewById(R.id.button_close);
//            close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    OnCloseToastListener listener = result.getOnCloseToastListener();
//                    if (listener != null) {
//                        listener.onClose(v);
//                        mHandler.sendEmptyMessage(HIDE_TOAST);
//                    }
//                }
//            });
//            ImageView icon = (ImageView) view.findViewById(R.id.icon);
//            if (IMNotToast.INTEGRAL_SYSTEM_TYPE == type) {
//                icon.setImageResource(R.drawable.ic_coin_success);
//            } else if (IMNotToast.RESUME_RED_PACKAGE_TYPE == type) {
//                icon.setImageResource(R.drawable.resume_red_package_icon);
//            }
            result.tn = new TN();
            result.tn.mNextView = view;
            result.tn.mDuration = duration;
            return result;
        }
    }

    /**
     * Description: 设置优先级
     *
     * @param priority 1 <= priority <= 3
     * @time 2018/1/9 下午5:40
     */
    public IMNotToast setPriority(int priority) {
        if (tn != null && 1 <= priority && priority >= 3) {
            tn.setPriority(priority);
        }
        return this;
    }

    public void show() {
        addShow(this.tn);
    }

    public void  hide() {
        mHandler.sendEmptyMessage(HIDE_TOAST);
    }

    public static void addShow(TN tn) {
        if (tn == null) {
            return;
        }
        Message message = new Message();
        message.what = ADD_TOAST;
        message.obj = tn;
        mHandler.sendMessage(message);
    }

    static class TN {
        private int priority = 1;
        View mView;
        View mNextView;
        int mDuration;
        WindowManager mWM;
        final LayoutParams mParams = new LayoutParams();

        public TN() {
            this.mParams.x = 0;
            this.mParams.y = -80;
            this.mParams.gravity = Gravity.LEFT | Gravity.TOP;
            this.mParams.height = LayoutParams.WRAP_CONTENT;
            this.mParams.width = LayoutParams.MATCH_PARENT;
            this.mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | LayoutParams.FLAG_NOT_FOCUSABLE;
            this.mParams.format = PixelFormat.RGBA_8888;
            this.mParams.windowAnimations = R.style.anim_window_view;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mParams.type = LayoutParams.TYPE_TOAST;
            }

            this.mParams.setTitle("Toast");
        }

        public void handleShow() {
            try {
                if (mView != mNextView) {
                    mView = mNextView;
                    Context context = mView.getContext().getApplicationContext();
                    if (context == null) {
                        context = mView.getContext();
                    }
                    this.mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    if (this.mView.getParent() != null) {
                        this.mWM.removeView(this.mView);
                    }
                    this.mWM.addView(this.mView, this.mParams);
                }
            } catch (Exception e) {
                Log.d("tanzhenxing", e.getMessage());
            }
            mHandler.sendEmptyMessageDelayed(HIDE_TOAST, mDuration);

        }

        public void handleHide() {
            try {
                if (mView != null) {
                    if (mView.getParent() != null) {
                        mWM.removeView(mView);
                    }
                    mView = null;
                }
            } catch (Exception e) {
                Log.d("tanzhenxing", e.getMessage());
            }
            mHandler.sendEmptyMessage(SHOW_TOAST);
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }


    @IntDef({
            IMNotToast.INTEGRAL_SYSTEM_TYPE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface TypeInt {
    }

}
