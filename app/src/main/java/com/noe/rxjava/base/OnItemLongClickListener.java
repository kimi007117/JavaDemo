package com.noe.rxjava.base;

import android.view.View;

/**
 * Created by lijie on 2019-11-19.
 */
public interface OnItemLongClickListener<T> {
    boolean onItemLongClick(View view, int position, T data);
}
