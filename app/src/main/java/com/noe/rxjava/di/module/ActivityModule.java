package com.noe.rxjava.di.module;

import com.noe.rxjava.mvp.contract.DouBanContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lijie24 on 2017/4/24.
 */

@Module
public class ActivityModule {
    private final DouBanContract.View mView;

    public ActivityModule(DouBanContract.View view) {
        this.mView = view;
    }

    @Provides
    DouBanContract.View provideActivity() {
        return mView;
    }
}
