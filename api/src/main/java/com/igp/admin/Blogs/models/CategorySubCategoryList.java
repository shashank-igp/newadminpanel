package com.igp.admin.Blogs.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 15/5/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategorySubCategoryList {

    @JsonProperty("category")
    private CategoryModel categoryModel;


    @JsonProperty("subcategory")
    private List<CategoryModel> subCategoryModelList;

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public List<CategoryModel> getSubCategoryModelList() {
        return subCategoryModelList;
    }

    public void setSubCategoryModelList(List<CategoryModel> subCategoryModelList) {
        this.subCategoryModelList = subCategoryModelList;
    }
}
