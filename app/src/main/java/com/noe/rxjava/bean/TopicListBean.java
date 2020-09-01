package com.noe.rxjava.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 58 on 2016/7/14.
 */
public class TopicListBean implements Serializable {
    public long cversion;
    public int code;
    public ArrayList<TopicBean> topics;
} 
