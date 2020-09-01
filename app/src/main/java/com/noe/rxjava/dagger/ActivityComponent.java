package com.noe.rxjava.dagger;

import com.noe.rxjava.DaggerActivity;

import dagger.Component;

/**
 * Created by lijie24 on 2017/1/17.
 */

@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

    // 注入
    void inject(DaggerActivity activity);
}
