package com.android.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.R;


/**
 * 普通类型的toast
 * Created by lijie on 2019/4/1.
 */
public class SimpleToast extends Toast {

    /**
     * 操作成功的对号
     */
    private static final int TYPE_SUCCESS = 0X1;

    /**
     * 操作失败的叹号
     */
    private static final int TYPE_FAIL = 0X2;

    /**
     * 操作提醒
     */
    private static final int TYPE_ALERT = 0X3;

    private static Toast attachToast;

    public SimpleToast(Context context) {
        super(context);
    }

    public static Toast makeText(final Context context, CharSequence text, int duration, int type) {
        if (attachToast == null) {
            attachToast = new SimpleToast(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
            TextView messageText = inflate.findViewById(R.id.text_message);
            setContent(messageText, text, type);
            attachToast.setView(inflate);
        } else {
            View view = attachToast.getView();
            TextView messageText = view.findViewById(R.id.text_message);
            setContent(messageText, text, type);
        }
        if (duration > 2000) {
            attachToast.setDuration(Toast.LENGTH_LONG);
        } else {
            attachToast.setDuration(Toast.LENGTH_SHORT);
        }
        attachToast.setGravity(Gravity.CENTER, 0, 0);

        return attachToast;
    }

    /**
     * 设置内容
     */
    private static void setContent(TextView messageText, CharSequence text, int type) {
        messageText.setText(text);
    }
}
