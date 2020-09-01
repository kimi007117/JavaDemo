package com.noe.rxjava.refresh.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.noe.rxjava.refresh.api.RefreshFooter;
import com.noe.rxjava.refresh.internal.InternalAbstract;


/**
 * 刷新底部包装
 * Created by lijie24 on 2019/8/30.
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter/*, InvocationHandler */{

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

//    @Override
//    public boolean setNoMoreData(boolean noMoreData) {
//        return mWrappedInternal instanceof RefreshFooter && ((RefreshFooter) mWrappedInternal).setNoMoreData(noMoreData);
//    }

}
