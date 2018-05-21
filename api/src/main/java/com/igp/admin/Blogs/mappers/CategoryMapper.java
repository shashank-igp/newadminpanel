package com.igp.admin.Blogs.mappers;

import com.igp.admin.Blogs.models.BlogResultModel;
import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.models.CategorySubCategoryModel;
import com.igp.admin.Blogs.utils.CategoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suditi on 3/5/18.
 */
public class CategoryMapper {
    private static final Logger logger = LoggerFactory.getLogger(CategoryMapper.class);

    public BlogResultModel createCategory(CategoryModel categoryModel){
        BlogResultModel blogResultModel = new BlogResultModel();
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            blogResultModel = categoryUtil.createCategory(categoryModel);
        }catch (Exception exception){
            logger.debug("error occured while creating Category ",exception);
        }
        return blogResultModel;
    }

    public BlogResultModel updateCategory(CategoryModel categoryModel){

        boolean result = false;
        BlogResultModel blogResultModel = new BlogResultModel();
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            blogResultModel = categoryUtil.updateCategory(categoryModel);
        }catch (Exception exception){
            logger.debug("error occured while updating Category post ",exception);
        }
        return blogResultModel;
    }

    public boolean deleteCategory(CategoryModel categoryModel){

        boolean result = false;
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            result = categoryUtil.deleteCategory(categoryModel);
        }catch (Exception exception){
            logger.debug("error occured while deleting Category post ",exception);
        }
        return result;
    }

    public List<CategorySubCategoryModel> getCategoryList(int fkAssociateId, int startLimit, int endLimit) {

        CategoryUtil categoryUtil = new CategoryUtil();
        List<CategorySubCategoryModel> categorySubCategoryLists = new ArrayList<>();
        try {
            categorySubCategoryLists = categoryUtil.getCategoryList(fkAssociateId, startLimit, endLimit);
        } catch (Exception exception) {
            logger.debug("error occured while getting list of Categories." + exception);
        }
        return categorySubCategoryLists;
    }

    public boolean validateCategory(int fkAssociateId, String categoryName, String subCategoryName){

        boolean result = false;
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            result = categoryUtil.validateCategory(fkAssociateId, categoryName, subCategoryName);
        }catch (Exception exception){
            logger.debug("error occured while validating category ",exception);
        }
        return result;
    }
}
