package com.noe.rxjava.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lijie24 on 2017/1/17.
 */

@Module
public class AppModule {
    private Context mContext;

    public AppModule(Context context){
        this.mContext = context;
    }

    @Provides
    Context providesContext(){
        return mContext;
    }
}
