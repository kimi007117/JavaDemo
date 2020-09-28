package com.noe.rxjava.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by lijie on 2018/5/30.
 */
public class RoundedCorners extends BitmapTransformation {
    private int mRadius;

    public RoundedCorners(Context context, int radius) {
        super(context);
        this.mRadius = radius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundedCorners(pool, toTransform);
    }

    private Bitmap roundedCorners(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap) {
        Bitmap.Config safeConfig = GJBitmapTransformationUtils.getAlphaSafeConfig(inBitmap);
        Bitmap toTransform = GJBitmapTransformationUtils.getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);
        result.setHasAlpha(true);

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());
        GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawRoundRect(rect, mRadius, mRadius, paint);
            canvas.setBitmap(null);
        } finally {
            GJBitmapTransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    @Override
    public String getId() {
        return "RoundTransformation(radius=" + mRadius + ")";
    }
}
