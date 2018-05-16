package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 15/5/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategorySubCategoryModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;

    @JsonProperty("subcategory")
    private List<CategoryModel> subCategoryModelList;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<CategoryModel> getSubCategoryModelList() {
        return subCategoryModelList;
    }

    public void setSubCategoryModelList(List<CategoryModel> subCategoryModelList) {
        this.subCategoryModelList = subCategoryModelList;
    }
}
