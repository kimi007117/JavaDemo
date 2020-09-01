package com.noe.rxjava.dagger;

import android.content.Context;

import dagger.Component;

/**
 * Created by lijie24 on 2017/1/17.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    Context proContext();
}
