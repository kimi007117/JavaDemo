package com.noe.rxjava.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 圆角转换并加上描边
 */
public class CircleWithBorderTransformation extends BitmapTransformation {

    private final int mBorderWidth;
    private final int mBorderColor;

    /**
     * @param context
     * @param borderWidth px
     * @param borderColor color,不是res ID
     */
    public CircleWithBorderTransformation(Context context, int borderWidth, int borderColor) {
        super(context);
        this.mBorderWidth = borderWidth;
        this.mBorderColor = borderColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCropWithBorder(pool, toTransform, outWidth, outHeight, mBorderWidth, mBorderColor);
    }

    @Override
    public String getId() {
        return String.format("%s_%s_%d", getClass().getName(), Integer.toHexString(mBorderColor), mBorderWidth);
    }


    static Bitmap circleCropWithBorder(BitmapPool pool, Bitmap source,
                                       int destWidth, int destHeight,
                                       int borderWidth, int borderColor) {
        int destMinEdge = Math.max(0, Math.min(destWidth - 2 * borderWidth, destHeight - 2 * borderWidth));
        float radius = destMinEdge / 2f;

        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();

        float scaleX = destMinEdge / (float) srcWidth;
        float scaleY = destMinEdge / (float) srcHeight;
        float maxScale = Math.max(scaleX, scaleY);

        float scaledWidth = maxScale * srcWidth;
        float scaledHeight = maxScale * srcHeight;
        float left = (destMinEdge - scaledWidth) / 2f + borderWidth;
        float top = (destMinEdge - scaledHeight) / 2f + borderWidth;

        RectF destRect = new RectF(left, top,
                left + scaledWidth, top + scaledHeight);

        // Alpha is required for this transformation.
        Bitmap toTransform = GJBitmapTransformationUtils.getAlphaSafeBitmap(pool, source);

        Bitmap result = pool.get(destMinEdge + 2 * borderWidth, destMinEdge + 2 * borderWidth, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(destMinEdge + 2 * borderWidth, destMinEdge + 2 * borderWidth, Bitmap.Config.ARGB_8888);
        }
        result.setHasAlpha(true);

        GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            // Draw a circle
            canvas.drawCircle(radius + borderWidth, radius + borderWidth, radius, GJBitmapTransformationUtils.CIRCLE_CROP_SHAPE_PAINT);
            // Draw the bitmap in the circle
            canvas.drawBitmap(toTransform, null, destRect, GJBitmapTransformationUtils.CIRCLE_CROP_BITMAP_PAINT);
            // Draw the border
            canvas.drawCircle(radius + borderWidth, radius + borderWidth, radius + borderWidth / 2, borderPaint);
            canvas.setBitmap(null);
        } finally {
            GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(source)) {
            pool.put(toTransform);
        }

        return result;
    }
}

