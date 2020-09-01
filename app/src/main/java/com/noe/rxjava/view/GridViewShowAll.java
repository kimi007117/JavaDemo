package com.noe.rxjava.view;

import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Created by 58 on 2016/9/20.
 */
public class GridViewShowAll extends GridView {
    public GridViewShowAll(android.content.Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
