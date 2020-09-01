package com.noe.rxjava.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by lijie on 2018/1/12.
 * 圆形图片转换并加上两个描边
 */
public class CircleWithTwoBorderTransformation extends BitmapTransformation {

    // 内环描边的宽度
    private int mInnerBorderWidth;
    // 内环描边的颜色
    private int mInnerBorderColor;
    // 外环描边的宽度
    private int mOuterBorderWidth;
    // 外环描边的颜色
    private int mOuterBorderColor;

    public CircleWithTwoBorderTransformation(Context context, int innerBorderWidth, int innerBorderColor, int outerBorderWidth, int outerBorderColor) {
        super(context);
        this.mInnerBorderWidth = innerBorderWidth;
        this.mInnerBorderColor = innerBorderColor;
        this.mOuterBorderWidth = outerBorderWidth;
        this.mOuterBorderColor = outerBorderColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCropWithBorder(pool, toTransform, outWidth, outHeight, mInnerBorderWidth, mInnerBorderColor, mOuterBorderWidth, mOuterBorderColor);
    }

    private Bitmap circleCropWithBorder(BitmapPool pool, Bitmap source, int destWidth, int destHeight,
                                        int innerBorderWidth, int innerBorderColor, int outerBorderWidth, int outerBorderColor) {
        int destMinEdge = Math.max(0, Math.min(destWidth - 2 * (innerBorderWidth + outerBorderWidth), destHeight - 2 * (innerBorderWidth + outerBorderWidth)));
        float radius = destMinEdge / 2f;

        Paint innerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerBorderPaint.setStyle(Paint.Style.STROKE);
        innerBorderPaint.setColor(innerBorderColor);
        innerBorderPaint.setStrokeWidth(innerBorderWidth);

        Paint outerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerBorderPaint.setStyle(Paint.Style.STROKE);
        outerBorderPaint.setColor(outerBorderColor);
        outerBorderPaint.setStrokeWidth(outerBorderWidth);

        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();

        float scaleX = destMinEdge / (float) srcWidth;
        float scaleY = destMinEdge / (float) srcHeight;
        float maxScale = Math.max(scaleX, scaleY);

        float scaledWidth = maxScale * srcWidth;
        float scaledHeight = maxScale * srcHeight;
        float left = (destMinEdge - scaledWidth) / 2f + (innerBorderWidth + outerBorderWidth);
        float top = (destMinEdge - scaledHeight) / 2f + (innerBorderWidth + outerBorderWidth);

        RectF destRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap toTransform = GJBitmapTransformationUtils.getAlphaSafeBitmap(pool, source);

        Bitmap result = pool.get(destMinEdge + 2 * (innerBorderWidth + outerBorderWidth), destMinEdge + 2 * (innerBorderWidth + outerBorderWidth), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(destMinEdge + 2 * (innerBorderWidth + outerBorderWidth), destMinEdge + 2 * (innerBorderWidth + outerBorderWidth), Bitmap.Config.ARGB_8888);
        }
        result.setHasAlpha(true);

        GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            // Draw a circle
            canvas.drawCircle(radius + (innerBorderWidth + outerBorderWidth), radius + (innerBorderWidth + outerBorderWidth), radius, GJBitmapTransformationUtils.CIRCLE_CROP_SHAPE_PAINT);
            // Draw the bitmap in the circle
            canvas.drawBitmap(toTransform, null, destRect, GJBitmapTransformationUtils.CIRCLE_CROP_BITMAP_PAINT);
            // Draw the inner border
            canvas.drawCircle(radius + (innerBorderWidth + outerBorderWidth), radius + (innerBorderWidth + outerBorderWidth), radius + innerBorderWidth / 2, innerBorderPaint);
            // Draw the outer border
            canvas.drawCircle(radius + (innerBorderWidth + outerBorderWidth), radius + (innerBorderWidth + outerBorderWidth), radius + innerBorderWidth + outerBorderWidth / 2, outerBorderPaint);
            canvas.setBitmap(null);
        } finally {
            GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(source)) {
            pool.put(toTransform);
        }


        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
