package com.android.core.list;

import java.util.LinkedList;

/**
 * 固定长度的list
 * Created by lijie on 2020/7/29.
 */
public class FixSizeLinkedList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 3292612616231532364L;
    // 定义缓存的容量
    private int capacity;

    public FixSizeLinkedList(int capacity) {
        super();
        this.capacity = capacity;
    }

    @Override
    public boolean add(T e) {
        // 超过长度，移除前面的一个
        if (size() + 1 > capacity) {
            super.removeFirst();
        }
        return super.add(e);
    }
}
