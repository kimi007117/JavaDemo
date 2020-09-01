package com.noe.rxjava.model.singleton;

/**
 * 静态内部类方式
 * Created by lijie on 2020-03-18.
 */
public class SingletonInternalClass {
    private SingletonInternalClass() {}

    public static SingletonInternalClass getInstance() {
        return SingletonInternalClassHolder.instance;
    }

    private static class SingletonInternalClassHolder {
        private static final SingletonInternalClass instance = new SingletonInternalClass();
    }
}
