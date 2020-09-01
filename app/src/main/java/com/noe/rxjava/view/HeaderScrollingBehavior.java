package com.noe.rxjava.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lijie on 2019-07-25.
 */
public class HeaderScrollingBehavior extends CoordinatorLayout.Behavior<View>{

    public HeaderScrollingBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
