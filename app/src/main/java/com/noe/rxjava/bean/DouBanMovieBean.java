package com.noe.rxjava.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lijie24 on 2017/4/20.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DouBanMovieBean implements Serializable {
    @JsonProperty("title")
    public String title;
    @JsonProperty("subjects")
    public ArrayList<MovieDetailBean> subjects;
}
