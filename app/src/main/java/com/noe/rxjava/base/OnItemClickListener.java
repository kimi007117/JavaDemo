package com.noe.rxjava.base;

import android.view.View;

/**
 * Created by lijie on 2019-11-19.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, int position, T data);
}
