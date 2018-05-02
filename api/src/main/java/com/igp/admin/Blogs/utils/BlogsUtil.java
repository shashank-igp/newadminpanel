package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by suditi on 2/5/18.
 */
public class BlogsUtil {
    private static final Logger logger = LoggerFactory.getLogger(BlogsUtil.class);

    public Integer createBlog(BlogMainModel blogMainModel){
        Integer blogId = null;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="insert into blog_post (title,created_by,content,url,published_date,fk_associate_id,status) "
                + " values ( ? ,? ,? ,? ,now() ,? ,? )";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, blogMainModel.getTitle());
            preparedStatement.setString(2, blogMainModel.getUser());
            preparedStatement.setString(3, blogMainModel.getContent());
            preparedStatement.setString(4, blogMainModel.getUrl());
            preparedStatement.setInt(5, blogMainModel.getFkAssociateId());
            preparedStatement.setInt(6, blogMainModel.getStatus());
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create tempOrder");
            } else
            {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                blogId = resultSet.getInt(1);
            }

        }catch (Exception exception){
            logger.debug("error occured while creating blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return blogId;
    }

}
