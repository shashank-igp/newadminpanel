package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 2/5/18.
 */
public class SeoBlogModel {
    @JsonProperty("seotitle")
    private String seoTitle;

    @JsonProperty("seodescription")
    private String seoDescription;

    @JsonProperty("seokeywords")
    private String seoKeywords;

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }
}
