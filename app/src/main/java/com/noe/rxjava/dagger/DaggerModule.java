package com.noe.rxjava.dagger;

import android.content.Context;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lijie24 on 2017/1/16.
 */

@Module //提供依赖对象的实例
public class DaggerModule {

    private Context mContext;

    public DaggerModule(Context context){
        this.mContext = context;
    }

    @Provides
    Context providerContext(){
        return mContext;
    }

    @Singleton
    @Provides // 关键字，标明该方法提供依赖对象
    Person providesPerson(Context context){
        //提供Person对象
        Log.i("dagger"," from Module");
        return new Person(context);
    }
}
