package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.Blogs.models.BlogResultModel;

import com.igp.admin.Blogs.models.CategoryModel;
import com.igp.admin.Blogs.models.CategorySubCategoryModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by suditi on 2/5/18.
 */
public class BlogsUtil {
    private static final Logger logger = LoggerFactory.getLogger(BlogsUtil.class);

    public BlogResultModel createBlog(BlogMainModel blogMainModel){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =  null;
        BlogResultModel blogResultModel = new BlogResultModel();
        try{
            boolean validUrl = checkUrlWithNoSpecialChar(blogMainModel.getUrl());
            if(validUrl==false){
                blogResultModel.setError(true);
                blogResultModel.setMessage("Invalid URL");
                return blogResultModel;
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO blog_post (title,created_by,description,content,url,published_date," +
                "fk_associate_id,status,blog_meta_title,blog_meta_keywords,blog_meta_description,flag_featured,sort_order) "
                + " VALUES ( ? ,? ,? ,? ,? ,now() ,? ,? ,? ,? , ? ,? ,?)";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, blogMainModel.getTitle());
            preparedStatement.setString(2, blogMainModel.getUser());
            preparedStatement.setString(3, blogMainModel.getShortDescription());
            preparedStatement.setString(4, blogMainModel.getDescription());
            preparedStatement.setString(5, blogMainModel.getUrl());
            preparedStatement.setInt(6, blogMainModel.getFkAssociateId());
            preparedStatement.setInt(7, blogMainModel.getStatus()); // by default keeping status as 1
            preparedStatement.setString(8, blogMainModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(9, blogMainModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(10, blogMainModel.getSeoModel().getSeoDescription());
            preparedStatement.setInt(11, blogMainModel.getFlagFeatured()); // by default keeping flag_featured as 0
            preparedStatement.setInt(12, blogMainModel.getSortOrder()); // by default keeping sort order as 1

            logger.debug("preparedstatement of insert blog_post : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create blog post");
            } else
            {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                blogMainModel.setId(resultSet.getInt(1));
                statement="INSERT INTO blog_post_image (blog_id,image_url,date_created,flag_featured) VALUES (? ,? ,now(),?)";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                preparedStatement.setString(2, blogMainModel.getImageUrl());
                preparedStatement.setInt(3, blogMainModel.getImageFlagFeatured());


                logger.debug("preparedstatement of insert blog_post_image : "+preparedStatement);

                status = preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Failed to create blog blog_post_image");
                }else {
                    blogResultModel =  insertUpdateBlogCatMap(blogMainModel);
                }
                logger.debug("New blog post created with url : "+blogMainModel.getUrl()+" id : "+blogMainModel.getId());
            }
            blogResultModel.setObject(blogMainModel.getUrl());
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
            blogResultModel.setError(true);
            blogResultModel.setMessage("error in insertion.");
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return blogResultModel;
    }
    public BlogResultModel updateBlog(BlogMainModel blogMainModel){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        BlogResultModel blogResultModel = new BlogResultModel();
        try{
            if(validateBlogUrl(blogMainModel.getFkAssociateId(),blogMainModel.getUrl()).isError()){
                blogResultModel.setError(true);
                blogResultModel.setMessage("Invalid URL.");
                return blogResultModel;
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE blog_post SET title = ?,created_by = ?,description = ?,content = ?,url = ?," +
                "fk_associate_id = ?,status = ?,blog_meta_title = ?,blog_meta_keywords = ?," +
                "blog_meta_description = ?,flag_featured = ?,sort_order = ? WHERE blog_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, blogMainModel.getTitle());
            preparedStatement.setString(2, blogMainModel.getUser());
            preparedStatement.setString(3, blogMainModel.getShortDescription());
            preparedStatement.setString(4, blogMainModel.getDescription());
            preparedStatement.setString(5, blogMainModel.getUrl());
            preparedStatement.setInt(6, blogMainModel.getFkAssociateId());
            preparedStatement.setInt(7, blogMainModel.getStatus());
            preparedStatement.setString(8, blogMainModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(9, blogMainModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(10, blogMainModel.getSeoModel().getSeoDescription());
            preparedStatement.setInt(11, blogMainModel.getFlagFeatured());
            preparedStatement.setInt(12, blogMainModel.getSortOrder());
            //  preparedStatement.setString(13, blogMainModel.getPublishDate());
            preparedStatement.setInt(13, blogMainModel.getId());
            logger.debug("preparedstatement of update blog_post : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            statement="UPDATE blog_post_image SET image_url=?,flag_featured=? WHERE blog_id=?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, blogMainModel.getImageUrl());
            preparedStatement.setInt(2, blogMainModel.getImageFlagFeatured());
            preparedStatement.setInt(3, blogMainModel.getId());

            logger.debug("preparedstatement of update blog_post_image : "+preparedStatement);

            status = preparedStatement.executeUpdate();
            blogResultModel =  insertUpdateBlogCatMap(blogMainModel);
            blogResultModel.setObject(blogMainModel);
            logger.debug("Blog post updated with id : " + blogMainModel.getId());


        }catch (Exception exception){
            blogResultModel.setError(true);
            blogResultModel.setMessage("Could not update blog.");
            logger.debug("error occured while updating blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return blogResultModel;
    }
    public boolean deleteBlog(BlogMainModel blogMainModel){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="DELETE FROM blog_post WHERE id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, blogMainModel.getId());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to delete blog post");
            } else {
                result = true;
                statement="DELETE FROM blog_cat_map WHERE blog_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                status = preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Failed to delete blog post");
                } else {
                    result = true;
                    statement="DELETE FROM blog_post_image WHERE blog_id = ?";
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.setInt(1, blogMainModel.getId());

                    logger.debug("Blog post deleted from blog_post_imagewith id : "+blogMainModel.getId());
                }
                logger.debug("Blog post deleted with id : "+blogMainModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while deleting blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public BlogResultModel insertUpdateBlogCatMap(BlogMainModel blogMainModel){
        BlogResultModel blogResultModel = new BlogResultModel();
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            Map<Integer,List<Integer>> categories = blogMainModel.getCategories();
            List<Integer> list = new ArrayList<>();
            statement="DELETE FROM blog_cat_map WHERE blog_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, blogMainModel.getId());
            preparedStatement.executeUpdate();

            for (Map.Entry<Integer, List<Integer>> entry : categories.entrySet())
            {
                int i = entry.getKey();
                list = entry.getValue();
                list.add(i);
                int size = list.size();
                while (size>0) {
                    statement = "INSERT INTO blog_cat_map (blog_id,categories_id) VALUES (? ,?) ON DUPLICATE KEY UPDATE blog_id = ?,categories_id = ?";
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.setInt(1, blogMainModel.getId());
                    preparedStatement.setInt(2, list.get(size-1));
                    preparedStatement.setInt(3, blogMainModel.getId());
                    preparedStatement.setInt(4, list.get(size-1));
                    logger.debug("preparedstatement of insert blog_cat_map : " + preparedStatement);
                    int status = preparedStatement.executeUpdate();
                    if (status == 0) {
                        logger.error("Failed to insert blog blog_cat_map");
                    }
                    size--;
                }
            }

        }catch (Exception exception){
            blogResultModel.setError(true);
            blogResultModel.setMessage("Could not update blog.");
            logger.debug("error occured while updating/inserting blog_cat_map "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return blogResultModel;
    }


    public boolean checkUrlWithNoSpecialChar(String url){
        boolean result = false;
        try{
            String pattern1 = "^[0-9A-Za-z-]*$";
            if (url==null || url.isEmpty() || !url.matches(pattern1)) {
                logger.debug("url is empty OR has some special characters.");
            }else {
                result = true;
            }

        }catch (Exception e){
            logger.debug("error occured while checking url has special chars "+e);
        }
        return result;
    }

    //this method will return true if passed (fkAssociateId, url) combination already exist
    public BlogResultModel validateBlogUrl(int fkAssociateId, String url){
        Connection connection = null;
        String statement = "";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        BlogResultModel result = new BlogResultModel();

        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select * from blog_post where fk_associate_id = ? AND url= ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, fkAssociateId);
            preparedStatement.setString(2, url);
            logger.debug("preparedStatement for validating blog url -> ", preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.first()){
                result.setError(false);
                result.setMessage("urlexist");
            }

        }catch (Exception e){
            logger.debug("error occured while validating url for blog.", e);
            result.setError(true);
            result.setMessage(e.getMessage());
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return result;
    }
}
