package com.noe.rxjava.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by lijie24 on 2017/4/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating implements Serializable {
    @JsonProperty("max")
    public float max;
    @JsonProperty("average")
    public float average;
}
