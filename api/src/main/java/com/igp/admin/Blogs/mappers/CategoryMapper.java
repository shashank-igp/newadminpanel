package com.igp.admin.Blogs.mappers;

import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.utils.CategoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by suditi on 3/5/18.
 */
public class CategoryMapper {
    private static final Logger logger = LoggerFactory.getLogger(CategoryMapper.class);
    public int createCategory(CategoryModel categoryModel){

        int id = 0;
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            id = categoryUtil.createCategory(categoryModel);
        }catch (Exception exception){
            logger.debug("error occured while creating Category ",exception);
        }
        return id;
    }

    public boolean updateCategory(CategoryModel categoryModel){

        boolean result = false;
        CategoryUtil categoryUtil = new CategoryUtil();
        try{
            result = categoryUtil.updateCategory(categoryModel);
        }catch (Exception exception){
            logger.debug("error occured while updating Category post ",exception);
        }
        return result;
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
}
