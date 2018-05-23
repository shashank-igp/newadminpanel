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
            BlogResultModel blogResultModel1 = validateBlogUrl(blogMainModel.getFkAssociateId(),blogMainModel.getUrl(), null);
            if(validUrl==false || blogResultModel1.isError()==true){
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
            } else{
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                blogMainModel.setId(resultSet.getInt(1));

                status = insertUpdateBlogImage(blogMainModel);
                if(status == 1){
                    blogResultModel =  insertUpdateBlogCatMap(blogMainModel);
                }else {
                    blogResultModel.setError(true);
                    blogResultModel.setMessage("error in insertion.");
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
            blogResultModel = validateBlogUrl(blogMainModel.getFkAssociateId(),blogMainModel.getUrl(), null);
            if(blogResultModel.isError()){
                // check if url exists for the same id
                BlogMainModel blogMainModel1 = (BlogMainModel) blogResultModel.getObject();

                if(blogMainModel.getId()!=blogMainModel1.getId()) {
                    blogResultModel.setError(true);
                    blogResultModel.setMessage("Invalid URL.");
                    return blogResultModel;
                }
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

            int status = preparedStatement.executeUpdate();

            status += insertUpdateBlogImage(blogMainModel);

            blogResultModel =  insertUpdateBlogCatMap(blogMainModel);
            if(blogResultModel.isError()==true && status==0){
                blogResultModel.setError(true);
                blogResultModel.setMessage("No blog found.");
            }
            else {
                blogResultModel.setError(false);
            }
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
    public boolean deleteBlog(int id){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="DELETE FROM blog_post WHERE blog_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            logger.debug("preparedstatement of delete blog_post : "+preparedStatement);

            Integer status = preparedStatement.executeUpdate();
            if (status != 0){

                statement="DELETE FROM blog_post_image WHERE blog_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, id);
                logger.debug("preparedstatement of delete blog_post_image : "+preparedStatement);
                status = preparedStatement.executeUpdate();

                statement="DELETE FROM blog_cat_map WHERE blog_id = ?";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, id);
                logger.debug("preparedstatement of delete blog_cat_map : "+preparedStatement);
                status = preparedStatement.executeUpdate();

                if (status == 0) {
                    logger.error("Failed to delete blog post");
                } else {
                    result = true;
                    logger.debug("Blog post deleted from blog_post_image with id : "+id);
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
    public int insertUpdateBlogImage(BlogMainModel blogMainModel){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            List<String> imageList = blogMainModel.getImageUrlList();
            statement="DELETE FROM blog_post_image WHERE blog_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, blogMainModel.getId());
            int delstatus = preparedStatement.executeUpdate();

            String statement1="INSERT INTO blog_post_image (blog_id,image_url,date_created,flag_featured,status) VALUES (? ,? ,now(),?,?)";
            preparedStatement = connection.prepareStatement(statement1);
            preparedStatement.setInt(1, blogMainModel.getId());
            preparedStatement.setString(2, blogMainModel.getImageUrl());
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, blogMainModel.getImageStatus());
            logger.debug("preparedstatement of insert blog_post_image : "+preparedStatement);
            int status = preparedStatement.executeUpdate();

            int size = imageList.size();
            while (size>0) {
                statement1="INSERT INTO blog_post_image (blog_id,image_url,date_created,flag_featured,status) VALUES (? ,? ,now(),?,?)";
                preparedStatement = connection.prepareStatement(statement1);
                preparedStatement.setInt(1, blogMainModel.getId());
                preparedStatement.setString(2, imageList.get(--size));
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 1);

                logger.debug("preparedstatement of insert blog_post_image non featured : "+preparedStatement);
                status+= preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Failed to insert blog blog_post_image");
                }
                // size--;

            }
            if(delstatus == 0 || delstatus == status){
                result = 1;
                // check that there was same num of rows earlier
            }

        }catch (Exception exception){

            logger.debug("error occured while updating/inserting blog_post_image "+exception);
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
            int status = preparedStatement.executeUpdate();

            for (Map.Entry<Integer, List<Integer>> entry : categories.entrySet())
            {
                int i = entry.getKey();
                list = entry.getValue();
                list.add(i);
                int size = list.size();
                if(status==size){
                    blogResultModel.setError(true);
                }
                while (size>0) {
                    statement = "INSERT INTO blog_cat_map (blog_id,categories_id) VALUES (? ,?) ON DUPLICATE KEY UPDATE blog_id = ?,categories_id = ?";
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.setInt(1, blogMainModel.getId());
                    preparedStatement.setInt(2, list.get(size-1));
                    preparedStatement.setInt(3, blogMainModel.getId());
                    preparedStatement.setInt(4, list.get(size-1));
                    logger.debug("preparedstatement of insert blog_cat_map : " + preparedStatement);
                    status = preparedStatement.executeUpdate();
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
    public BlogResultModel validateBlogUrl(int fkAssociateId, String url, String imageUrl){
        Connection connection = null;
        String statement = "";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        BlogResultModel result = new BlogResultModel();
        BlogMainModel blogMainModel = new BlogMainModel();
        try{
            blogMainModel.setId(0);
            connection = Database.INSTANCE.getReadOnlyConnection();
            if(url != null && !url.isEmpty()){
                statement = "select * from blog_post where fk_associate_id = "+fkAssociateId+" AND url= '"+url+ "'";
            }else if(imageUrl != null && !imageUrl.isEmpty()){
                statement = "select * from blog_post_image where image_url= '"+imageUrl + "'";
            }

            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedStatement for validating blog url -> " + preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.first()){
                blogMainModel.setId(resultSet.getInt("blog_id"));
                result.setError(true);
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
        result.setObject(blogMainModel);
        return result;
    }

    public BlogListResponseModel getListBlog(int fkAssociateId, int id, int start, int end){
        Connection connection = null;
        String statement, statementCategories="";
        ResultSet resultSet =  null, resultSetCategories = null;
        PreparedStatement preparedStatement = null, preparedStatement1;
        List<BlogMainModel> blogMainModelList = new ArrayList<>();
        String column = "";
        BlogListResponseModel blogListResponseModel = new BlogListResponseModel();
        SeoBlogModel seoBlogModel = new SeoBlogModel();

        try{
            if(id != -1){
                ///id is passed, return data for blog id = 'id'
                statement= "select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,bc.categories_id,bc.parent_id,bc.categories_name,bc.categories_name_for_url,b.description,b.url,b.status,"
                    + " group_concat(DISTINCT(if(bpm.flag_featured=0,bpm.image_url,null))  separator ',') AS non_featured_image_url, group_concat(DISTINCT(if(bpm.flag_featured=1,bpm.image_url,null))  separator ',') AS image_url, "
                    + " b.content,b.created_by,  b.flag_featured, b.sort_order, bpm.status,home_meta_title,home_meta_keywords,home_meta_description, bmh.home_name FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id "
                    + " JOIN (select * from blog_categories where fk_associate_id = 5 and status = 1 order by"
                    + " sort_order desc) as bc on bcm.categories_id=bc.categories_id LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id "
                    + " AND bpm.status = 1 JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id"
                    + " WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1 AND b.blog_id= "+ id +" GROUP BY b.blog_id ORDER BY published_date DESC";

                statementCategories = "select pst.blog_id, bct.categories_id, bct.categories_name, bct.categories_name_for_url ,bct2.categories_id as p_cat_id, bct2.categories_name as p_cat_name, bct2.categories_name_for_url as p_cat_name_for_url from "
                    +"blog_post pst join blog_cat_map bcm on pst.blog_id = bcm.blog_id join blog_categories bct on bcm.categories_id = bct.categories_id "
                    +"left join blog_categories bct2 on bct.parent_id = bct2.categories_id where pst.status = 1 AND bct.status = 1  AND pst.blog_id = "+id+" order by pst.blog_id, bct.parent_id DESC";

            }else {
                // homepage, return all blogs
                statement="select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,bc.categories_id,bc.parent_id,bc.categories_name,bc.categories_name_for_url,b.description,b.url,b.status," +
                    " group_concat(DISTINCT(if(bpm.flag_featured=0,bpm.image_url,null))  separator ',') AS non_featured_image_url, group_concat(DISTINCT(if(bpm.flag_featured=1,bpm.image_url,null))  separator ',') AS image_url, "+
                    " bpm.status,home_meta_title,home_meta_keywords,home_meta_description, bmh.home_name FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id " +
                    " JOIN (select * from blog_categories where fk_associate_id = "+fkAssociateId+" and status = 1 order by" +
                    " sort_order desc) as bc on bcm.categories_id=bc.categories_id LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id  " +
                    " AND bpm.status = 1 JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id" +
                    " WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1 GROUP BY b.blog_id ORDER BY published_date DESC limit "+start+","+end;

                statementCategories = "select pst.blog_id, bct.categories_id, bct.categories_name, bct.categories_name_for_url ,bct2.categories_id as p_cat_id, bct2.categories_name as p_cat_name, bct2.categories_name_for_url as p_cat_name_for_url from "
                    +"blog_post pst join blog_cat_map bcm on pst.blog_id = bcm.blog_id join blog_categories bct on bcm.categories_id = bct.categories_id "
                    +"left join blog_categories bct2 on bct.parent_id = bct2.categories_id where pst.status = 1 AND bct.status = 1 order by pst.blog_id, bct.parent_id DESC";

            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedstatement of blog list : "+preparedStatement);

            preparedStatement1 = connection.prepareStatement(statementCategories, ResultSet.TYPE_SCROLL_INSENSITIVE);
            logger.debug("preparedstatement of categories list : "+preparedStatement1);

            resultSetCategories =  preparedStatement1.executeQuery();

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
                blogMainModel.setImageStatus(resultSet.getInt("bpm.status"));
                blogMainModel.setStatus(resultSet.getInt("b.status"));
                if(resultSet.getString("image_url") != null){
                    //featured image url
                    List<String> featuredImageUrls = Arrays.asList(resultSet.getString("image_url").split(","));
                    blogMainModel.setImageUrl(featuredImageUrls.get(0));
                }
                if(resultSet.getString("non_featured_image_url") != null){
                    List<String> nonFeaturedImageUrls = Arrays.asList(resultSet.getString("non_featured_image_url").split(","));
                    blogMainModel.setImageUrlList(nonFeaturedImageUrls);
                }
                if(id != -1){
                    blogMainModel.setUser(resultSet.getString("b.created_by"));
                    blogMainModel.setFlagFeatured(resultSet.getInt("b.flag_featured"));
                    blogMainModel.setSortOrder(resultSet.getInt("b.sort_order"));
                    blogMainModel.setDescription(resultSet.getString("b.content"));
                    blogMainModel.setShortDescription(resultSet.getString("b.description"));
                }

                //set blog category List(bloglist) object here
                List<CategorySubCategoryModel> categoryList = new ArrayList<>();
                CategorySubCategoryModel categorySubCategoryModel ;
                while (resultSetCategories.next()){

                    if(resultSetCategories.getInt("blog_id") == blogMainModel.getId()){
                        categorySubCategoryModel= new CategorySubCategoryModel();
                        if(resultSetCategories.getInt("p_cat_id") == 0){
                            //current category not having parent category
                            categorySubCategoryModel.setId(resultSetCategories.getInt("categories_id"));
                            categorySubCategoryModel.setTitle(resultSetCategories.getString("categories_name"));
                            categorySubCategoryModel.setUrl(resultSetCategories.getString("categories_name_for_url"));

                        }else{
                            //current category having parent category, collect all subcategories under current parent category
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

                            while (resultSetCategories.next()) {//check whether next category has same parent
                                if (resultSetCategories.getInt("p_cat_id") == categorySubCategoryModel.getId()) {
                                    //next category also has same parent, so add it to sub category list
                                    categoryModel2 = new CategoryModel();
                                    categoryModel2.setId(resultSetCategories.getInt("categories_id"));
                                    categoryModel2.setTitle(resultSetCategories.getString("categories_name"));
                                    categoryModel2.setUrl(resultSetCategories.getString("categories_name_for_url"));
                                    subCategoryModelList.add(categoryModel2);
                                } else {
                                    //next category have different or no parent, so set resultSet to previous row & break the loop
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

                blogMainModelList.add(blogMainModel);
            }
            if(id == -1){
                statement="SELECT count(DISTINCT b.blog_id) total FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id " +
                    "JOIN blog_categories bc ON bcm.categories_id = bc.categories_id  AND b.fk_associate_id=bc.fk_associate_id WHERE b.fk_associate_id = "+fkAssociateId +
                    " AND bc.status = 1 AND b.status = 1 "+ column;
                preparedStatement = connection.prepareStatement(statement);
                logger.debug("preparedstatement of blog list count : "+preparedStatement);

                resultSet = preparedStatement.executeQuery();
                if(resultSet.first()){
                    blogListResponseModel.setCount(resultSet.getInt("total"));
                }
            }else{
                blogListResponseModel.setCount(1);//as returning only one blog
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

    public BlogResultModel updateBlogStatus(int id, int status){
        String statement;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        BlogResultModel blogResultModel = new BlogResultModel();

        try{
            statement = "update blog_post set status = "+ status +" where blog_id = "+id;

            connection = Database.INSTANCE.getReadWriteConnection();
            preparedStatement = connection.prepareStatement(statement);

            int rowUpdated = preparedStatement.executeUpdate();

            if(rowUpdated == 0 ){
                blogResultModel.setError(true);
                blogResultModel.setMessage("No blog found.");
            }else{
                blogResultModel.setError(false);
            }
        }catch(Exception e){
            blogResultModel.setError(true);
            blogResultModel.setMessage("Could not update blog status.");
            logger.debug("error occured while updating blog post ",e);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return blogResultModel;
    }
}
