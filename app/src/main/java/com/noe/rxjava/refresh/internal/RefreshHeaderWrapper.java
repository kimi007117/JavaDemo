package com.noe.rxjava.refresh.internal;

import android.view.View;

import com.noe.rxjava.refresh.api.RefreshHeader;

/**
 * Created by lijie on 2019-08-30.
 */
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader {
    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }
}
