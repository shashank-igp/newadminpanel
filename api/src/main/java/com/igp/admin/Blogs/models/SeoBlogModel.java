package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 2/5/18.
 */
public class SeoBlogModel {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("fkasid")
    private Integer fkAssociateId;

    @JsonProperty("seotitle")
    private String seoTitle;

    @JsonProperty("seodescription")
    private String seoDescription;

    @JsonProperty("seokeywords")
    private String seoKeywords;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFkAssociateId() {
        return fkAssociateId;
    }

    public void setFkAssociateId(Integer fkAssociateId) {
        this.fkAssociateId = fkAssociateId;
    }

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
