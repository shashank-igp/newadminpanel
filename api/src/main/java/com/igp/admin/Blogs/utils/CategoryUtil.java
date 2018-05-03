package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try{
            tempUrl = createUrlUsingTitle(categoryModel.getName());
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO blog_categories (categories_name,categories_name_alt,parent_id,sort_order,categories_description,categories_name_for_url," +
                "categories_meta_title,categories_meta_keywords,categories_meta_description,categories_introduction_text,categories_introduction_down_text,categories_status) "
                + " VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, categoryModel.getName());
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
            preparedStatement.setString(1, categoryModel.getName());
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


    public String createUrlUsingTitle(String title){
        String url = "";
        String correctStr = title.trim();
        try{
            String pattern1 = "^[0-9A-Za-z]*$";
            if (!title.matches(pattern1)) {
                logger.debug("Address has some unmatched special char.Let's replace it with a space.");
                Pattern pattern = Pattern.compile("^[0-9A-Za-z]*$");
                // pattern allows a set of special chars,apha-numeric replace it with hyphen
                int count = 0;
                int length = correctStr.length();
                int i = 0;
                while (i < length) {
                    Matcher m = pattern.matcher(title.charAt(i)+"");
                    if (!m.matches()) {
                        //  logger.debug("Unmatched character is : "+title.charAt(i)+" at index : "+i);
                        int index = i + 1 ;
                        count++;
                        correctStr = correctStr.substring(0, i) + "-" + correctStr.substring(index);
                        // replace the un matched char by a space.
                    }
                    i++;
                }
                url = correctStr;
                logger.debug("Finally the returned string from special char match is : "+url);
            }
        }catch (Exception e){
            logger.debug("error occured while creating url ",e);
        }
        return url;
    }
}
