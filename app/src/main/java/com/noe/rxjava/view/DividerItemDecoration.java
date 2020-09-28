package com.noe.rxjava.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by lijie24 on 2017/6/16.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    /**
     * item之间分割线的高度，默认为1
     */
    private int mDividerHeight = 1;

    /**
     * 绘制item分割线的画笔，和设置其属性
     * 来绘制个性分割线
     */
    private Paint mPaint;


    private int mOrientation;

    public DividerItemDecoration(int dividerHeight, int color, int orientation) {
        this.mDividerHeight = dividerHeight;
        //抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(c, parent);
        } else {
            drawHorizontalLine(c, parent);
        }
    }


    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 2; i < childSize-2; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    //画竖线
    public void drawVerticalLine(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }
}