package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by suditi on 7/5/18.
 */
@JsonDeserialize
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BlogListResponseModel {
    @JsonProperty("seo")
    private SeoBlogModel seoModel;

    @JsonProperty("bloglist")
    private List<BlogMainModel> blogList;

    @JsonProperty("count")
    private int count;

    @JsonProperty("breadcrumb")
    private BreadcrumbBlogModel breadcrumb;

    @JsonProperty("home")
    private CategoryModel homeCategory;

    public SeoBlogModel getSeoModel() {
        return seoModel;
    }

    public void setSeoModel(SeoBlogModel seoModel) {
        this.seoModel = seoModel;
    }

    public List<BlogMainModel> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<BlogMainModel> blogList) {
        this.blogList = blogList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BreadcrumbBlogModel getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(BreadcrumbBlogModel breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public CategoryModel getHomeCategory() {
        return homeCategory;
    }

    public void setHomeCategory(CategoryModel homeCategory) {
        this.homeCategory = homeCategory;
    }
}
