package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 2/5/18.
 */
@JsonDeserialize
@JsonSerialize
public class BlogMainModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("user")
    private String user;

    @JsonProperty("content")
    private String content;

    @JsonProperty("url")
    private String url;

    @JsonProperty("publishdate")
    private String publishDate;

    @JsonProperty("parentcategory")
    private String parentCategory;

    @JsonProperty("subcategory")
    private String subCategory;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("prevpost")
    private String prevPost;

    @JsonProperty("nextpost")
    private String nextPost;

    @JsonProperty("seo")
    private SeoBlogModel seoModel;

    @JsonProperty("fkasid")
    private int fkAssociateId;

    @JsonProperty("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPrevPost() {
        return prevPost;
    }

    public void setPrevPost(String prevPost) {
        this.prevPost = prevPost;
    }

    public String getNextPost() {
        return nextPost;
    }

    public void setNextPost(String nextPost) {
        this.nextPost = nextPost;
    }

    public SeoBlogModel getSeoModel() {
        return seoModel;
    }

    public void setSeoModel(SeoBlogModel seoModel) {
        this.seoModel = seoModel;
    }

    public int getFkAssociateId() {
        return fkAssociateId;
    }

    public void setFkAssociateId(int fkAssociateId) {
        this.fkAssociateId = fkAssociateId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

