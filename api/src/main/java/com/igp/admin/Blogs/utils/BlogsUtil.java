package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suditi on 2/5/18.
 */
public class BlogsUtil {
    private static final Logger logger = LoggerFactory.getLogger(BlogsUtil.class);

    public String createBlog(BlogMainModel blogMainModel){
        String url = "", tempUrl = "";
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =  null;
        try{
            tempUrl = createUrlUsingTitle(blogMainModel.getTitle());
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO blog_post (title,created_by,description,content,url,published_date," +
                "fk_associate_id,status,blog_meta_title,blog_meta_keywords,blog_meta_description,flag_featured,sort_order) "
                + " VALUES ( ? ,? ,? ,? ,? ,now() ,? ,? ,? ,? , ? ,? ,?)";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, blogMainModel.getTitle());
            preparedStatement.setString(2, blogMainModel.getUser());
            preparedStatement.setString(3, blogMainModel.getDescription());
            preparedStatement.setString(4, blogMainModel.getContent());
            preparedStatement.setString(5, tempUrl);
            preparedStatement.setInt(6, blogMainModel.getFkAssociateId());
            preparedStatement.setInt(7, blogMainModel.getStatus());
            preparedStatement.setString(8, blogMainModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(9, blogMainModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(10, blogMainModel.getSeoModel().getSeoDescription());
            preparedStatement.setInt(11, blogMainModel.getFlagFeatured());
            preparedStatement.setInt(12, blogMainModel.getSortOrder());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create blog post");
            } else
            {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                blogMainModel.setId(resultSet.getInt(1));
                statement="INSERT INTO blog_post_image (blog_id,image_url,date_created) VALUES (? ,? ,now())";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                preparedStatement.setString(2, blogMainModel.getImageUrl());
                status = preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Failed to create blog blog_post_image");
                }else {
                    Iterator<Integer> iterator = blogMainModel.getCategoryId().iterator();
                    while (iterator.hasNext()) {
                        statement="INSERT INTO blog_cat_map (blog_id,categories_id) VALUES (? ,?)";
                        preparedStatement = connection.prepareStatement(statement);
                        preparedStatement.setInt(1, blogMainModel.getId());
                        preparedStatement.setInt(2, iterator.next());
                        status = preparedStatement.executeUpdate();
                        if (status == 0) {
                            logger.error("Failed to create blog blog_cat_map");
                        }
                    }
                }
                url = tempUrl;
                logger.debug("New blog post created with url : "+url+" id : "+blogMainModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return url;
    }
    public boolean updateBlog(BlogMainModel blogMainModel){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE blog_post SET title = ?,created_by = ?,description = ?,content = ?,url = ?," +
                "fk_associate_id = ?,status = ?,blog_meta_title = ?,blog_meta_keywords = ?," +
                "blog_meta_description = ?,flag_featured = ?,sort_order = ?,published_date ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, blogMainModel.getTitle());
            preparedStatement.setString(2, blogMainModel.getUser());
            preparedStatement.setString(3, blogMainModel.getDescription());
            preparedStatement.setString(4, blogMainModel.getContent());
            preparedStatement.setString(5, blogMainModel.getUrl());
            preparedStatement.setInt(6, blogMainModel.getFkAssociateId());
            preparedStatement.setInt(7, blogMainModel.getStatus());
            preparedStatement.setString(8, blogMainModel.getSeoModel().getSeoTitle());
            preparedStatement.setString(9, blogMainModel.getSeoModel().getSeoKeywords());
            preparedStatement.setString(10, blogMainModel.getSeoModel().getSeoDescription());
            preparedStatement.setInt(11, blogMainModel.getFlagFeatured());
            preparedStatement.setInt(12, blogMainModel.getSortOrder());
            preparedStatement.setString(13, blogMainModel.getPublishDate());
            preparedStatement.setInt(14, blogMainModel.getId());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update blog post");
            } else {
                result = true;
                logger.debug("Blog post updated with id : "+blogMainModel.getId());
            }
        }catch (Exception exception){
            logger.debug("error occured while updating blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
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
