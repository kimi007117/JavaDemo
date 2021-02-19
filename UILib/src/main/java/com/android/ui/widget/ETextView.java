package com.android.ui.widget;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 行间距适配方案
 * Created by lijie on 2021/2/3.
 */
public class ETextView extends AppCompatTextView {

    public ETextView(Context context) {
        super(context);
    }

    public ETextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ETextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 排除每行文字间的padding
     *
     * @param text
     */
    public void setCustomText(CharSequence text) {
        if (text == null) {
            return;
        }

        // 获得视觉定义的每行文字的行高
        int lineHeight = (int) getTextSize();

        SpannableStringBuilder ssb;
        if (text instanceof SpannableStringBuilder) {
            ssb = (SpannableStringBuilder) text;
        } else {
            ssb = new SpannableStringBuilder(text);
        }
        // 设置LineHeightSpan
        ssb.setSpan(new ExcludeInnerLineSpaceSpan(lineHeight),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 调用系统setText()方法
        setText(ssb);
    }
}
