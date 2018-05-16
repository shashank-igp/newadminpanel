package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 2/5/18.
 */
@JsonDeserialize
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogMainModel {

    @JsonIgnore
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("user")
    private String user;

    @JsonProperty("description")
    private String description;

    @JsonProperty("shortdescription")
    private String shortDescription;

    @JsonProperty("url")
    private String url;

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("publishdate")
    private String publishDate;

    @JsonProperty("parentcategory")
    private CategoryModel parentCategory;

    @JsonProperty("subcategory")
    private CategoryModel subCategory;

    @JsonProperty("prevpost")
    private BlogMainModel prevPost;

    @JsonProperty("nextpost")
    private BlogMainModel nextPost;

    @JsonProperty("seo")
    private SeoBlogModel seoModel;

    @JsonProperty("fkasid")
    private Integer fkAssociateId;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("sortorder")
    private Integer sortOrder;

    @JsonProperty("flagfeatured")
    private Integer flagFeatured;

    @JsonProperty("categories")
    private Map<Integer,List<Integer>> categories;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public CategoryModel getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryModel parentCategory) {
        this.parentCategory = parentCategory;
    }

    public CategoryModel getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(CategoryModel subCategory) {
        this.subCategory = subCategory;
    }

    public BlogMainModel getPrevPost() {
        return prevPost;
    }

    public void setPrevPost(BlogMainModel prevPost) {
        this.prevPost = prevPost;
    }

    public BlogMainModel getNextPost() {
        return nextPost;
    }

    public void setNextPost(BlogMainModel nextPost) {
        this.nextPost = nextPost;
    }

    public SeoBlogModel getSeoModel() {
        return seoModel;
    }

    public void setSeoModel(SeoBlogModel seoModel) {
        this.seoModel = seoModel;
    }

    public Integer getFkAssociateId() {
        return fkAssociateId;
    }

    public void setFkAssociateId(Integer fkAssociateId) {
        this.fkAssociateId = fkAssociateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getFlagFeatured() {
        return flagFeatured;
    }

    public void setFlagFeatured(Integer flagFeatured) {
        this.flagFeatured = flagFeatured;
    }

    public Map<Integer,List<Integer>> getCategories() {
        return categories;
    }

    public void setCategories(Map<Integer,List<Integer>> categories) {
        this.categories = categories;
    }
}

