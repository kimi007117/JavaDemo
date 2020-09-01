package com.noe.rxjava.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.noe.rxjava.base.RxApplication;

/**
 * Created by lijie24 on 2017/3/13.
 */

public class CustomLinearLayoutManager extends RecyclerView.LayoutManager {


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < getChildCount(); i++) {
            if (i == 0) {
                int offsetX = (RxApplication.getWidth() - dip2px(351)) / 2 + dip2px(9);
                int offsetY = 0;
                View scrap = recycler.getViewForPosition(0);
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);  // 计算此碎片view包含边距的尺寸
                int width = dip2px(220);
                int height = dip2px(226);
//        int width = getDecoratedMeasuredWidth(scrap);  // 获取此碎片view包含边距和装饰的宽度width
//        int height = getDecoratedMeasuredHeight(scrap); // 获取此碎片view包含边距和装饰的高度height
                layoutDecorated(scrap, offsetX, offsetY, offsetX + width, offsetY + height); // Important！布局到RecyclerView容器中，所有的计算都是为了得出任意position的item的边界来布局
            }


        }

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private static int dip2px(float dpValue) {
        final float scale = RxApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
