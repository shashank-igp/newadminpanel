package com.igp.admin.Blogs.utils;

import com.igp.admin.Blogs.models.BlogMainModel;
import com.igp.admin.Blogs.models.BlogResultModel;

import com.igp.admin.Blogs.models.*;
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
            statement="DELETE FROM blog_post WHERE blog_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, blogMainModel.getId());
            logger.debug("preparedstatement of delete blog_post : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            if (status != 0){

                statement="DELETE FROM blog_post_image WHERE blog_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                logger.debug("preparedstatement of delete blog_post_image : "+preparedStatement);
                status = preparedStatement.executeUpdate();

                statement="DELETE FROM blog_cat_map WHERE blog_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                logger.debug("preparedstatement of delete blog_cat_map : "+preparedStatement);
                status = preparedStatement.executeUpdate();

                if (status == 0) {
                    logger.error("Failed to delete blog post");
                } else {
                    result = true;
                    logger.debug("Blog post deleted from blog_post_image with id : "+blogMainModel.getId());
                }
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
            logger.debug("preparedStatement for validating blog url -> " + preparedStatement);

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

    public BlogListResponseModel getListBlog(int fkAssociateId, boolean isCategory, String categoryName, String subCategoryName, int start, int end){
        Connection connection = null;
        String statement, statementCategories="";
        ResultSet resultSet =  null, resultSetCategories = null;
        PreparedStatement preparedStatement = null, preparedStatement1;
        List<BlogMainModel> blogMainModelList = new ArrayList<>();
        String column = "";
        BlogListResponseModel blogListResponseModel = new BlogListResponseModel();
        SeoBlogModel seoBlogModel = new SeoBlogModel();
        CategoryModel categoryModel = new CategoryModel();
        try{
            if(isCategory){
                statement="select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,b.description,b.url,b.status," +
                    " bpm.image_url,bmh.home_meta_title,bmh.home_meta_keywords,bmh.home_meta_description FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id" +
                    " and bcm.categories_id = "+categoryModel.getId()+" LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id AND bpm.flag_featured = 1  AND bpm.status = 1 " +
                    "JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1" +
                    " ORDER BY published_date DESC limit "+start+","+end;

                column = " AND bcm.categories_id = "+categoryModel.getId();
            }else {
                // homepage
                statement="select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,bc.categories_id,bc.parent_id,bc.categories_name,bc.categories_name_for_url,b.description,b.url,b.status," +
                    " bpm.image_url,home_meta_title,home_meta_keywords,home_meta_description, bmh.home_name FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id " +
                    "JOIN (select * from blog_categories where fk_associate_id = "+fkAssociateId+" and status = 1 order by" +
                    " sort_order desc) as bc on bcm.categories_id=bc.categories_id LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id AND bpm.flag_featured = 1 " +
                    " AND bpm.status = 1 JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id" +
                    " WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1 GROUP BY b.blog_id ORDER BY published_date DESC limit "+start+","+end;

                statementCategories = "select pst.blog_id, bct.categories_id, bct.categories_name, bct.categories_name_for_url ,bct2.categories_id as p_cat_id, bct2.categories_name as p_cat_name, bct2.categories_name_for_url as p_cat_name_for_url from "
                    +"blog_post pst join blog_cat_map bcm on pst.blog_id = bcm.blog_id join blog_categories bct on bcm.categories_id = bct.categories_id "
                    +"left join blog_categories bct2 on bct.parent_id = bct2.categories_id where pst.status = 1 AND bct.status = 1 order by pst.blog_id, bct.parent_id DESC";

            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedstatement of blog list : "+preparedStatement);

            if(!isCategory){
                preparedStatement1 = connection.prepareStatement(statementCategories, ResultSet.TYPE_SCROLL_INSENSITIVE);
                logger.debug("preparedstatement of categories list : "+preparedStatement1);

                resultSetCategories =  preparedStatement1.executeQuery();
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BlogMainModel blogMainModel = new BlogMainModel();

                seoBlogModel.setSeoTitle(resultSet.getString("home_meta_title"));
                seoBlogModel.setSeoKeywords(resultSet.getString("home_meta_keywords"));
                seoBlogModel.setSeoDescription(resultSet.getString("home_meta_description"));

                blogMainModel.setFkAssociateId(fkAssociateId);
                blogMainModel.setFkAssociateName(resultSet.getString("bmh.home_name"));
                blogMainModel.setId(resultSet.getInt("b.blog_id"));
                blogMainModel.setTitle(resultSet.getString("b.title"));
                blogMainModel.setPublishDate(resultSet.getString("pub_date"));
                blogMainModel.setUrl(resultSet.getString("b.url"));
                blogMainModel.setImageUrl(resultSet.getString("bpm.image_url"));

                if(!isCategory){
                    //set blog category List object here
                    List<CategorySubCategoryModel> categoryList = new ArrayList<>();
                    CategorySubCategoryModel categorySubCategoryModel ;
                    while (resultSetCategories.next()){

                        if(resultSetCategories.getInt("blog_id") == blogMainModel.getId()){
                            categorySubCategoryModel= new CategorySubCategoryModel();
                            if(resultSetCategories.getInt("p_cat_id") == 0){
                                //current category not having parent cat
                                categorySubCategoryModel.setId(resultSetCategories.getInt("categories_id"));
                                categorySubCategoryModel.setTitle(resultSetCategories.getString("categories_name"));
                                categorySubCategoryModel.setUrl(resultSetCategories.getString("categories_name_for_url"));

                            }else{
                                //current category having parent cat, collect all subcategories under current parent category
                                categorySubCategoryModel.setId(resultSetCategories.getInt("p_cat_id"));
                                categorySubCategoryModel.setTitle(resultSetCategories.getString("p_cat_name"));
                                categorySubCategoryModel.setUrl(resultSetCategories.getString("p_cat_name_for_url"));
                                //now set sub category list
                                List<CategoryModel> subCategoryModelList = new ArrayList<>();
                                CategoryModel categoryModel2 = new CategoryModel();
                                categoryModel2.setId(resultSetCategories.getInt("categories_id"));
                                categoryModel2.setTitle(resultSetCategories.getString("categories_name"));
                                categoryModel2.setUrl(resultSetCategories.getString("categories_name_for_url"));
                                subCategoryModelList.add(categoryModel2);

                                while (resultSetCategories.next()) {
                                    if (resultSetCategories.getInt("p_cat_id") == categorySubCategoryModel.getId()) {
                                        //next category also has same parent, so add it to sub category list
                                        categoryModel2 = new CategoryModel();
                                        categoryModel2.setId(resultSetCategories.getInt("categories_id"));
                                        categoryModel2.setTitle(resultSetCategories.getString("categories_name"));
                                        categoryModel2.setUrl(resultSetCategories.getString("categories_name_for_url"));
                                        subCategoryModelList.add(categoryModel2);
                                    } else {
                                        //next category have different or no parent, so set resultSet to previous & break the loop
                                        resultSetCategories.previous();
                                        break;
                                    }
                                }
                                categorySubCategoryModel.setSubCategoryModelList(subCategoryModelList);
                            }
                            categoryList.add(categorySubCategoryModel);
                        }
                    }
                    blogMainModel.setCategoryList(categoryList);
                    resultSetCategories.beforeFirst();
                }
                blogMainModel.setImageUrl(resultSet.getString("bpm.image_url"));

                blogMainModelList.add(blogMainModel);
            }
            statement="SELECT count(DISTINCT b.blog_id) total FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id " +
                "JOIN blog_categories bc ON bcm.categories_id = bc.categories_id  AND b.fk_associate_id=bc.fk_associate_id WHERE b.fk_associate_id = "+fkAssociateId +
                " AND b.status = 1 "+ column;
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedstatement of blog list count : "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.first()){
                blogListResponseModel.setCount(resultSet.getInt("total"));
            }

            blogListResponseModel.setBlogList(blogMainModelList);
            blogListResponseModel.setSeoModel(seoBlogModel);
        }catch (Exception exception){
            logger.debug("error occured while getting blog post ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return blogListResponseModel;
    }

}
