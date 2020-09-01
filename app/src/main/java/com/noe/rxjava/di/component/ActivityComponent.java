package com.noe.rxjava.di.component;

import com.noe.rxjava.DouBanActivity;
import com.noe.rxjava.di.module.ActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lijie24 on 2017/4/24.
 */
@Singleton
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(DouBanActivity douBanActivity);
}
