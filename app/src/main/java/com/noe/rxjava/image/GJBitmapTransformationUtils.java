package com.noe.rxjava.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lizy on 17-8-8.
 */

class GJBitmapTransformationUtils {
    static final int PAINT_FLAGS = Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG;
    static final int CIRCLE_CROP_PAINT_FLAGS = PAINT_FLAGS | Paint.ANTI_ALIAS_FLAG;
    static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
    static final Paint CIRCLE_CROP_BITMAP_PAINT;

    // See #738.
    private static final List<String> MODELS_REQUIRING_BITMAP_LOCK = Arrays.asList("XT1097", "XT1085");

    /**
     * https://github.com/bumptech/glide/issues/738 On some devices (Moto X with android 5.1) bitmap
     * drawing is not thread safe.
     * This lock only locks for these specific devices. For other types of devices the lock is always
     * available and therefore does not impact performance
     */
    static final Lock BITMAP_DRAWABLE_LOCK =
            MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL)
                    && Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1
                    ? new ReentrantLock() : new NoLock();

    static {
        CIRCLE_CROP_BITMAP_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
        CIRCLE_CROP_BITMAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private GJBitmapTransformationUtils() {
        // Utility class.
    }


    static Bitmap getAlphaSafeBitmap(@NonNull BitmapPool pool,
                                     @NonNull Bitmap maybeAlphaSafe) {
        if (Bitmap.Config.ARGB_8888.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(),
                Bitmap.Config.ARGB_8888);
        if (argbBitmap == null) {
            argbBitmap = Bitmap.createBitmap(maybeAlphaSafe.getWidth(),
                    maybeAlphaSafe.getHeight(), Bitmap.Config.ARGB_8888);
        }
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*pain*/);

        return argbBitmap;
    }

    public static Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }

        return Bitmap.Config.ARGB_8888;
    }

    private static final class NoLock implements Lock {

        NoLock() {
        }

        @Override
        public void lock() {
            // do nothing
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            // do nothing
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
            // do nothing
        }

        @NonNull
        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }
    }
}
