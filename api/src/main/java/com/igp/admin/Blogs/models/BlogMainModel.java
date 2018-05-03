package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by suditi on 2/5/18.
 */
@JsonDeserialize
@JsonSerialize
public class BlogMainModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("categoryid")
    private List<Integer> categoryId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("user")
    private String user;

    @JsonProperty("description")
    private String description;

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

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("seo")
    private SeoBlogModel seoModel;

    @JsonProperty("fkasid")
    private int fkAssociateId;

    @JsonProperty("status")
    private int status;

    @JsonProperty("sortorder")
    private int sortOrder;

    @JsonProperty("flagfeatured")
    private int flagFeatured;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getFlagFeatured() {
        return flagFeatured;
    }

    public void setFlagFeatured(int flagFeatured) {
        this.flagFeatured = flagFeatured;
    }
}

