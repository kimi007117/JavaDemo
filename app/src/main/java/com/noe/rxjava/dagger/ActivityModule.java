package com.noe.rxjava.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lijie24 on 2017/1/17.
 */

@Module
public class ActivityModule {
    @Provides
    Person providePerson(Context context){
        //　此方法需要Context 对象
        return new Person(context);
    }

}
