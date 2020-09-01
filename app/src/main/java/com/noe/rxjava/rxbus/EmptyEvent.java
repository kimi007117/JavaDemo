package com.noe.rxjava.rxbus;

/**
 * Created by lijie on 2020-05-06.
 */
public class EmptyEvent implements Event {
    private String action;

    public EmptyEvent(String action) {
        this.action = action;
    }

    public String action() {
        return this.action;
    }
}
