package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 9/5/18.
 */

@JsonDeserialize
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreadcrumbBlogModel {
    @JsonProperty("category")
    private CategoryModel category;

    @JsonProperty("subcategory")
    private CategoryModel subcategory;

    @JsonProperty("flaghome")
    private Integer flagHome;

    @JsonIgnore
    private CategoryModel homecategory;

    @JsonProperty("flagerror")
    private Integer flagError;

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public CategoryModel getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(CategoryModel subcategory) {
        this.subcategory = subcategory;
    }

    public Integer getFlagHome() {
        return flagHome;
    }

    public void setFlagHome(Integer flagHome) {
        this.flagHome = flagHome;
    }

    public CategoryModel getHomecategory() {
        return homecategory;
    }

    public void setHomecategory(CategoryModel homecategory) {
        this.homecategory = homecategory;
    }

    public Integer getFlagError() {
        return flagError;
    }

    public void setFlagError(Integer flagError) {
        this.flagError = flagError;
    }

}
