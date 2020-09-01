package com.noe.rxjava.refresh.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.noe.rxjava.refresh.api.RefreshHeader;
import com.noe.rxjava.refresh.internal.InternalAbstract;


/**
 * 刷新头部包装
 * Created by lijie24 on 2019/8/30.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
