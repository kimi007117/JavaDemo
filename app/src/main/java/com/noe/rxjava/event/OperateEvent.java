package com.noe.rxjava.event;

/**
 * Created by lijie on 2019-10-12.
 */
public class OperateEvent<T> {

    public int code;

    public T data;

    public OperateEvent(int code,T data){
        this.code = code;
        this.data = data;
    }
}
