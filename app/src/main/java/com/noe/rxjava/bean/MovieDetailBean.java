package com.noe.rxjava.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by lijie24 on 2017/4/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetailBean implements Serializable {
    @JsonProperty("title")
    public String title;
    @JsonProperty("year")
    public String year;
    @JsonProperty("collect_count")
    public int collectCount;
    @JsonProperty("id")
    public String id;
    @JsonProperty("rating")
    public Rating rating;
    @JsonProperty("images")
    public ImageBean images;
}
