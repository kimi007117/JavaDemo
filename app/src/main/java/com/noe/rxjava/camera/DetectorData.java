package com.noe.rxjava.camera;

import android.graphics.Rect;

/**
 * Created by lijie on 2018/4/19.
 * 识别后的数据
 */
public class DetectorData {

    //原始数据
    public byte[] faceData;

    //脸部区域集合
    public Rect[] faceRectList;

    //光线强度
    public float lightIntensity;

    //脸部数量
    public int facesCount;

    //距离
    public float distance;

}
