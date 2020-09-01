package com.noe.rxjava.dagger;

import com.noe.rxjava.DaggerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lijie24 on 2017/1/16.
 */

@Singleton
@Component(modules = DaggerModule.class)
public interface TestComponent {
    //定义注入的方法
    void inject(DaggerActivity daggerActivity);
}
