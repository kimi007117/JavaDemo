package com.noe.rxjava.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.noe.rxjava.R;

import java.lang.ref.WeakReference;

/**
 * Created by lijie on 2018/5/9.
 */
public class SweetTopAlter {
    private static WeakReference<Activity> activityWeakReference;

    private TopAlertDialog alert;

    /**
     * Constructor
     */
    private SweetTopAlter() {
        //Utility classes should not be instantiated
    }

    public static SweetTopAlter create(@NonNull final Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null!");
        }

        final SweetTopAlter SweetTopAlter = new SweetTopAlter();

        //Hide current TopAlertDialog, if one is active
        SweetTopAlter.clearCurrent(activity);

        SweetTopAlter.setActivity(activity);
        SweetTopAlter.setAlert(new TopAlertDialog(activity));

        return SweetTopAlter;
    }

    /**
     * Cleans up the currently showing TopAlertDialog view, if one is present
     *
     * @param activity The current Activity
     */
    public static void clearCurrent(@NonNull final Activity activity) {
        if (activity == null) {
            return;
        }

        try {
            final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

            //Find all TopAlertDialog Views in Parent layout
            for (int i = 0; i < decorView.getChildCount(); i++) {
                final TopAlertDialog childView = decorView.getChildAt(i) instanceof TopAlertDialog ? (TopAlertDialog) decorView.getChildAt(i) : null;
                if (childView != null && childView.getWindowToken() != null) {
                    ViewCompat.animate(childView).alpha(0).withEndAction(getRemoveViewRunnable(childView));
                }
            }

        } catch (Exception ex) {
            Log.e(SweetTopAlter.class.getClass().getSimpleName(), Log.getStackTraceString(ex));
        }
    }

    /**
     * Hides the currently showing TopAlertDialog view, if one is present
     */
    public static void hide() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            clearCurrent(activityWeakReference.get());
        }
    }

    private static Runnable getRemoveViewRunnable(final TopAlertDialog childView) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    if (childView != null) {
                        ((ViewGroup) childView.getParent()).removeView(childView);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
                }
            }
        };
    }

    /**
     * Check if an TopAlertDialog is currently showing
     *
     * @return True if an TopAlertDialog is showing, false otherwise
     */
    public static boolean isShowing() {
        boolean isShowing = false;
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            isShowing = activityWeakReference.get().findViewById(R.id.flAlertBackground) != null;
        }

        return isShowing;
    }

    /**
     * Shows the TopAlertDialog, after it's built
     *
     * @return An TopAlertDialog object check can be altered or hidden
     */
    public TopAlertDialog show() {
        //This will get the Activity Window's DecorView
        if (getActivityWeakReference() != null) {
            getActivityWeakReference().get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add the new TopAlertDialog to the View Hierarchy
                    final ViewGroup decorView = getActivityDecorView();
                    if (decorView != null && getAlert().getParent() == null) {
                        decorView.addView(getAlert());
                    }
                }
            });
        }

        return getAlert();
    }

    /**
     * Sets the title of the TopAlertDialog
     *
     * @param titleId Title String Resource
     * @return This SweetTopAlter
     */
    public SweetTopAlter setTitle(@StringRes final int titleId) {
        if (getAlert() != null) {
            getAlert().setTitle(titleId);
        }

        return this;
    }

    /**
     * Set Title of the TopAlertDialog
     *
     * @param title Title as a String
     * @return This SweetTopAlter
     */
    public SweetTopAlter setTitle(final String title) {
        if (getAlert() != null) {
            getAlert().setTitle(title);
        }

        return this;
    }


    /**
     * Sets the TopAlertDialog Text
     *
     * @param textId Text String Resource
     * @return This SweetTopAlter
     */
    public SweetTopAlter setText(@StringRes final int textId) {
        if (getAlert() != null) {
            getAlert().setText(textId);
        }

        return this;
    }

    /**
     * Sets the TopAlertDialog Text
     *
     * @param text String of TopAlertDialog Text
     * @return This SweetTopAlter
     */
    public SweetTopAlter setText(final String text) {
        if (getAlert() != null) {
            getAlert().setText(text);
        }

        return this;
    }


    /**
     * Set the TopAlertDialog's Background Colour
     *
     * @param colorInt Colour int value
     * @return This SweetTopAlter
     */
    public SweetTopAlter setBackgroundColorInt(@ColorInt final int colorInt) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundColor(colorInt);
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Background Colour
     *
     * @param colorResId Colour Resource Id
     * @return This SweetTopAlter
     */
    public SweetTopAlter setBackgroundColorRes(@ColorRes final int colorResId) {
        if (getAlert() != null && getActivityWeakReference() != null) {
            getAlert().setAlertBackgroundColor(ContextCompat.getColor(getActivityWeakReference().get(), colorResId));
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Background Drawable
     *
     * @param drawable Drawable
     * @return This SweetTopAlter
     */
    public SweetTopAlter setBackgroundDrawable(final Drawable drawable) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundDrawable(drawable);
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Background Drawable Resource
     *
     * @param drawableResId Drawable Resource Id
     * @return This SweetTopAlter
     */
    public SweetTopAlter setBackgroundResource(@DrawableRes final int drawableResId) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundResource(drawableResId);
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Icon
     *
     * @param iconId The Drawable's Resource Idw
     * @return This SweetTopAlter
     */
    public SweetTopAlter setIcon(@DrawableRes final int iconId) {
        if (getAlert() != null) {
            getAlert().setIcon(iconId);
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Icon
     *
     * @param bitmap The Bitmap object to use for the icon.
     * @return This SweetTopAlter
     */
    public SweetTopAlter setIcon(@NonNull final Bitmap bitmap) {
        if (getAlert() != null) {
            getAlert().setIcon(bitmap);
        }

        return this;
    }

    /**
     * Set the TopAlertDialog's Icon
     *
     * @param drawable The Drawable to use for the icon.
     * @return This SweetTopAlter
     */
    public SweetTopAlter setIcon(@NonNull final Drawable drawable) {
        if (getAlert() != null) {
            getAlert().setIcon(drawable);
        }

        return this;
    }


    /**
     * Hide the Icon
     *
     * @return This SweetTopAlter
     */
    public SweetTopAlter hideIcon() {
        if (getAlert() != null) {
            getAlert().getIcon().setVisibility(View.GONE);
        }

        return this;
    }

    /**
     * Set the onClickListener for the TopAlertDialog
     *
     * @param onClickListener The onClickListener for the TopAlertDialog
     * @return This SweetTopAlter
     */
    public SweetTopAlter setOnClickListener(@NonNull final View.OnClickListener onClickListener) {
        if (getAlert() != null) {
            getAlert().setOnClickListener(onClickListener);
        }

        return this;
    }

    /**
     * Set the on screen duration of the TopAlertDialog
     *
     * @param milliseconds The duration in milliseconds
     * @return This SweetTopAlter
     */
    public SweetTopAlter setDuration(@NonNull final long milliseconds) {
        if (getAlert() != null) {
            getAlert().setDuration(milliseconds);
        }
        return this;
    }


    /**
     * Set whether to show the icon in the TopAlertDialog or not
     *
     * @param showIcon True to show the icon, false otherwise
     * @return This SweetTopAlter
     */
    public SweetTopAlter showIcon(final boolean showIcon) {
        if (getAlert() != null) {
            getAlert().showIcon(showIcon);
        }
        return this;
    }

    /**
     * Enable or disable infinite duration of the TopAlertDialog
     *
     * @param infiniteDuration True if the duration of the TopAlertDialog is infinite
     * @return This SweetTopAlter
     */
    public SweetTopAlter enableInfiniteDuration(final boolean infiniteDuration) {
        if (getAlert() != null) {
            getAlert().setEnableInfiniteDuration(infiniteDuration);
        }
        return this;
    }

    /**
     * Sets the TopAlertDialog Shown Listener
     *
     * @param listener OnShowAlertListener of TopAlertDialog
     * @return This SweetTopAlter
     */
    public SweetTopAlter setOnShowListener(@NonNull final TopAlertDialog.OnShowAlertListener listener) {
        if (getAlert() != null) {
            getAlert().setOnShowListener(listener);
        }
        return this;
    }

    /**
     * Sets the TopAlertDialog Hidden Listener
     *
     * @param listener OnHideAlertListener of TopAlertDialog
     * @return This SweetTopAlter
     */
    public SweetTopAlter setOnHideListener(@NonNull final TopAlertDialog.OnHideAlertListener listener) {
        if (getAlert() != null) {
            getAlert().setOnHideListener(listener);
        }
        return this;
    }

    /**
     * Enables swipe to dismiss
     *
     * @return This SweetTopAlter
     */
    public SweetTopAlter enableSwipeToDismiss() {
        if (getAlert() != null) {
            getAlert().enableSwipeToDismiss();
        }
        return this;
    }

    /**
     * Enable or Disable Vibration
     *
     * @param enable True to enable, False to disable
     * @return This SweetTopAlter
     */
    public SweetTopAlter enableVibration(final boolean enable) {
        if (getAlert() != null) {
            getAlert().setVibrationEnabled(enable);
        }

        return this;
    }

    /**
     * Disable touch events outside of the TopAlertDialog
     *
     * @return This SweetTopAlter
     */
    public SweetTopAlter disableOutsideTouch() {
        if (getAlert() != null) {
            getAlert().disableOutsideTouch();
        }

        return this;
    }


    /**
     * Gets the TopAlertDialog associated with the SweetTopAlter
     *
     * @return The current TopAlertDialog
     */
    private TopAlertDialog getAlert() {
        return alert;
    }

    /**
     * Sets the TopAlertDialog
     *
     * @param alert The TopAlertDialog to be references and maintained
     */
    private void setAlert(final TopAlertDialog alert) {
        this.alert = alert;
    }

    private WeakReference<Activity> getActivityWeakReference() {
        return activityWeakReference;
    }

    /**
     * Get the enclosing Decor View
     *
     * @return The Decor View of the Activity the SweetTopAlter was called from
     */
    private ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        if (getActivityWeakReference() != null && getActivityWeakReference().get() != null) {
            decorView = (ViewGroup) getActivityWeakReference().get().getWindow().getDecorView();
        }

        return decorView;
    }

    /**
     * Creates a weak reference to the calling Activity
     *
     * @param activity The calling Activity
     */
    private void setActivity(@NonNull final Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }
}
