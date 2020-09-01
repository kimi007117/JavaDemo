package com.noe.rxjava.bean;

/**
 * Created by lijie on 2018/12/22.
 */
public class Page<T> {
    public long firstTimeStamp;
    public int count;
    public int page;
    public int limit;
    public int pages;
    public T data;
}
