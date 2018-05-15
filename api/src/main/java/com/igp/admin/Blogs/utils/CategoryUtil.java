package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.models.CategorySubCategoryList;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suditi on 3/5/18.
 */
public class CategoryUtil {
    private static final Logger logger = LoggerFactory.getLogger(CategoryUtil.class);

    public int createCategory(CategoryModel categoryModel){
        String url = "", tempUrl = "";
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =  null;
        BlogsUtil blogsUtil = new BlogsUtil();
        try{
            tempUrl = blogsUtil.createUrlUsingTitle(categoryModel.getTitle());
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO blog_categories (categories_name,categories_name_alt,parent_id,sort_order,categories_description,categories_name_for_url," +
                "categories_meta_title,categories_meta_keywords,categories_meta_description,categories_introduction_text,categories_introduction_down_text,categories_status) "
                + " VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, categoryModel.getTitle());
            preparedStatement.setString(2, categoryModel.getNameAlt());
            preparedStatement.setInt(3, categoryModel.getParentId());
            preparedStatement.setInt(4, categoryModel.getSortOrder());
            preparedStatement.setString(5, categoryModel.getDescription());
            preparedStatement.setString(6, tempUrl);
            preparedStatement.setString(7, categoryModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(8, categoryModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(9, categoryModel.getSeoModel().getSeoDescription());
            preparedStatement.setString(10, categoryModel.getIntroText());
            preparedStatement.setString(11, categoryModel.getIntroDownText());
            preparedStatement.setInt(12, categoryModel.getStatus());
            logger.debug("preparedstatement of insert blog_categories : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create Category post");
            } else
            {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                categoryModel.setId(resultSet.getInt(1));
                url = tempUrl;
                logger.debug("New Category created with url : "+url+" id : "+categoryModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while creating Category : ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return categoryModel.getId();
    }
    public boolean updateCategory(CategoryModel categoryModel){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE blog_categories SET categories_name=?,categories_name_alt=?,parent_id=?,sort_order=?,categories_description=?,categories_name_for_url=?," +
                "categories_meta_title=?,categories_meta_keywords=?,categories_meta_description=?,categories_introduction_text=?,categories_introduction_down_text=?,categories_status=? WHERE categories_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, categoryModel.getTitle());
            preparedStatement.setString(2, categoryModel.getNameAlt());
            preparedStatement.setInt(3, categoryModel.getParentId());
            preparedStatement.setInt(4, categoryModel.getSortOrder());
            preparedStatement.setString(5, categoryModel.getDescription());
            preparedStatement.setString(6, categoryModel.getUrl());
            preparedStatement.setString(7, categoryModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(8, categoryModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(9, categoryModel.getSeoModel().getSeoDescription());
            preparedStatement.setString(10, categoryModel.getIntroText());
            preparedStatement.setString(11, categoryModel.getIntroDownText());
            preparedStatement.setInt(12, categoryModel.getStatus());
            preparedStatement.setInt(13, categoryModel.getId());
            logger.debug("preparedstatement of update blog_categories : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update Category ");
            } else {
                result = true;
                logger.debug("Category updated with id : "+categoryModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while updating Category ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean deleteCategory(CategoryModel categoryModel){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="DELETE FROM blog_categories WHERE categories_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, categoryModel.getId());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to delete Category ");
            } else {
                result = true;
                logger.debug("Category deleted with id : "+categoryModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting Category ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean validateCategory(int fkAssociateId, String categoryName, String subCategoryName){
        Connection connection = null;
        String statement="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement = null;
        boolean  result = false;
        try{
            statement = "select bct.categories_id as p_cat, bct2.categories_id as ch_cat, bct.fk_associate_id, bct.categories_name as p_cat_name, bct2.categories_name as ch_cat_name "
                +" from blog_categories bct,blog_categories bct2"
                +" where bct.fk_associate_id = bct2.fk_associate_id AND bct.categories_id = bct2.parent_id AND bct.status=1 AND bct2.status=1"
                +" AND bct2.categories_name_for_url = ? AND bct.categories_name_for_url = ? AND bct.fk_associate_id = ?";
            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, subCategoryName);
            preparedStatement.setString(2, categoryName);
            preparedStatement.setInt(3, fkAssociateId);
            logger.debug("preparedstatement of finding valid category and subcategory : "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                result = true;
            }

        }catch (Exception exception){
            logger.debug("error occured while finding valid category and subcategory",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return result;
    }
    public List<CategorySubCategoryList> getCategoryList(int fkAssociateId, int startLimit, int endLimit){
        Connection connection = null;
        String statement="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement = null;
        List<CategorySubCategoryList> categorySubCategoryLists = new ArrayList<>();
        try{
                statement="SELECT bct.categories_id AS cat_id, bct.categories_name AS cat_name," +
                    "bct.categories_name_for_url AS cat_url, group_concat(bct2.categories_id,','," +
                    " bct2.categories_name,',',bct2.categories_name_for_url separator ':') AS subcat" +
                    " FROM blog_categories bct LEFT JOIN blog_categories bct2 on bct.categories_id = " +
                    "bct2.parent_id AND bct.status=1 AND bct2.status=1 and bct.fk_associate_id = " +
                    "bct2.fk_associate_id WHERE bct.fk_associate_id = ? AND bct.parent_id = 0 GROUP BY " +
                    "bct.categories_id ORDER BY bct.sort_order limit ?,?";

            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, fkAssociateId);
            preparedStatement.setInt(2, startLimit);
            preparedStatement.setInt(3, endLimit);

            logger.debug("preparedstatement of finding category list : "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategorySubCategoryList categorySubCategoryList = new CategorySubCategoryList();
                String categoryStrArray[] = null;
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setUrl(resultSet.getString("cat_url"));
                categoryModel.setTitle(resultSet.getString("cat_name"));
                categoryModel.setId(resultSet.getInt("cat_id"));
                categorySubCategoryList.setCategoryModel(categoryModel);


                String subcat = resultSet.getString("subcat");
                if(subcat!=null){
                    List<CategoryModel> subcategoryList = new ArrayList<>();
                    categoryStrArray = subcat.split(":");
                    for(int i = 0; i< categoryStrArray.length ; i++){
                        CategoryModel subcategory = new CategoryModel();
                        subcategory.setId(new Integer(categoryStrArray[i].split(",")[0]));
                        subcategory.setTitle(categoryStrArray[i].split(",")[1]);
                        subcategory.setUrl(categoryStrArray[i].split(",")[2]);
                        subcategoryList.add(subcategory);
                    }
                    categorySubCategoryList.setSubCategoryModelList(subcategoryList);
                }
                categorySubCategoryLists.add(categorySubCategoryList);
            }

        }catch (Exception exception){
            logger.debug("error occured while getting category ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return categorySubCategoryLists;
    }
}
