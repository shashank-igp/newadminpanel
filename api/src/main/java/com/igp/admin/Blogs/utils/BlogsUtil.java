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
                statement="INSERT INTO blog_post_image (blog_id,image_url,date_created,flag_featured) VALUES (? ,? ,now(),1)";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, blogMainModel.getId());
                preparedStatement.setString(2, blogMainModel.getImageUrl());

                logger.debug("preparedstatement of insert blog_post_image : "+preparedStatement);

                status = preparedStatement.executeUpdate();
                if (status == 0) {
                    logger.error("Failed to create blog blog_post_image");
                }else {
                    List<Integer> list = new ArrayList<>();
                    for (Map.Entry<Integer, List<Integer>> entry : blogMainModel.getCategories().entrySet())
                    {
                        int i = entry.getKey();
                        list = entry.getValue();
                        list.add(i);
                        int size = list.size();
                        while (size>0) {
                            statement = "INSERT INTO blog_cat_map (blog_id,categories_id) VALUES (? ,?)";
                            preparedStatement = connection.prepareStatement(statement);
                            preparedStatement.setInt(1, blogMainModel.getId());
                            preparedStatement.setInt(2, list.get(size-1));
                            logger.debug("preparedstatement of insert blog_cat_map : " + preparedStatement);
                            status = preparedStatement.executeUpdate();
                            if (status == 0) {
                                logger.error("Failed to insert blog blog_cat_map");
                            }
                            size--;
                        }
                    }
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
            preparedStatement.setString(13, blogMainModel.getPublishDate());
            preparedStatement.setInt(14, blogMainModel.getId());
            logger.debug("preparedstatement of update blog_post : "+preparedStatement);

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
        String statement;
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement = null;
        List<BlogMainModel> blogMainModelList = new ArrayList<>();
        String column = "";
        BlogListResponseModel blogListResponseModel = new BlogListResponseModel();
        SeoBlogModel seoBlogModel = new SeoBlogModel();
        CategoryModel categoryModel = new CategoryModel();
        BreadcrumbBlogModel breadcrumbBlogModel = new BreadcrumbBlogModel();
        try{
            if(isCategory==true){
                categoryModel = getCategoryDetails(fkAssociateId,isCategory,categoryName,subCategoryName);
                statement="select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,b.description,b.url,b.status," +
                    " bpm.image_url,bmh.home_meta_title,bmh.home_meta_keywords,bmh.home_meta_description FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id" +
                    " and bcm.categories_id = "+categoryModel.getId()+" LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id AND bpm.flag_featured = 1  AND bpm.status = 1 " +
                    "JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1" +
                    " ORDER BY published_date DESC limit "+start+","+end;

                column = " AND bcm.categories_id = "+categoryModel.getId();
                if(categoryModel.getParentId()!=0){
                    // while finding prev and next post, keep it in account.
                    breadcrumbBlogModel.setFlagHome(0);
                    breadcrumbBlogModel.setFlagError(0);
                    breadcrumbBlogModel.setSubcategory(categoryModel);
                    breadcrumbBlogModel.setCategory(getCategoryDetails(fkAssociateId, isCategory, categoryName, ""));
                }else {
                    breadcrumbBlogModel.setCategory(categoryModel);
                    breadcrumbBlogModel.setFlagHome(0);
                    breadcrumbBlogModel.setFlagError(0);
                }
            }else {
                // homepage
                statement="select b.blog_id,b.title,DATE_FORMAT(b.published_date,'%d-%b-%Y') as pub_date,bc.categories_id,bc.parent_id,bc.categories_name,bc.categories_name_for_url,b.description,b.url,b.status," +
                    " bpm.image_url,home_meta_title,home_meta_keywords,home_meta_description FROM blog_post b JOIN blog_cat_map bcm ON b.blog_id = bcm.blog_id " +
                    "JOIN (select * from blog_categories where fk_associate_id = "+fkAssociateId+" and status = 1 and parent_id = 0 order by" +
                    " sort_order desc) as bc on bcm.categories_id=bc.categories_id LEFT JOIN blog_post_image bpm ON b.blog_id = bpm.blog_id AND bpm.flag_featured = 1 " +
                    " AND bpm.status = 1 JOIN blog_meta_home bmh ON b.fk_associate_id = bmh.fk_associate_id" +
                    " WHERE b.fk_associate_id = "+fkAssociateId+" AND b.status = 1 GROUP BY b.blog_id ORDER BY published_date DESC limit "+start+","+end;

                breadcrumbBlogModel = getBreadcrumbForBlog(fkAssociateId,isCategory,categoryName,subCategoryName,"",true);

            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedstatement of blog list : "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryModel categoryModel1 = new CategoryModel();
                BlogMainModel blogMainModel = new BlogMainModel();

                seoBlogModel.setSeoTitle(resultSet.getString("home_meta_title"));
                seoBlogModel.setSeoKeywords(resultSet.getString("home_meta_keywords"));
                seoBlogModel.setSeoDescription(resultSet.getString("home_meta_description"));

                blogMainModel.setFkAssociateId(fkAssociateId);
                blogMainModel.setId(resultSet.getInt("b.blog_id"));
                blogMainModel.setTitle(resultSet.getString("b.title"));
                blogMainModel.setShortDescription(resultSet.getString("b.description"));
                blogMainModel.setPublishDate(resultSet.getString("pub_date"));
                blogMainModel.setUrl(resultSet.getString("b.url"));

                if(isCategory==false){
                    categoryModel1.setTitle(resultSet.getString("bc.categories_name"));
                    categoryModel1.setUrl(resultSet.getString("bc.categories_name_for_url"));
                    categoryModel1.setParentId(0);
                    categoryModel1.setId(resultSet.getInt("bc.categories_id"));
                    blogMainModel.setParentCategory(categoryModel1);
                }else {
                    blogMainModel.setParentCategory(breadcrumbBlogModel.getCategory());
                    if(categoryModel.getParentId()!=0){
                        blogMainModel.setSubCategory(breadcrumbBlogModel.getSubcategory());
                    }
                }
                blogMainModel.setImageUrl(resultSet.getString("bpm.image_url"));
                blogMainModel.setStatus(resultSet.getInt("b.status"));

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

            if(isCategory==false){
                // homepage so need to add it's title and url.
                blogListResponseModel.setHomeCategory(breadcrumbBlogModel.getHomecategory());
            }
            blogListResponseModel.setBreadcrumb(breadcrumbBlogModel);
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

    public BreadcrumbBlogModel getBreadcrumbForBlog(int fkAssociateId, boolean isCategory, String categoryName, String subCategoryName, String blogUrl, boolean flagList){
        Connection connection = null;
        String statement="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement = null;
        BreadcrumbBlogModel breadcrumbBlogModel = new BreadcrumbBlogModel();
        CategoryModel categoryModel = new CategoryModel();
        CategoryModel categoryModel1 = new CategoryModel();
        try{
            breadcrumbBlogModel.setFlagError(1);
            if(isCategory == false && flagList==true){
                // it means request from blog listing homepage
                breadcrumbBlogModel.setFlagHome(1);
                statement="select home_id,home_name,home_name_for_url from blog_meta_home where fk_associate_id = "
                    +fkAssociateId+" and status = 1 ";
                connection = Database.INSTANCE.getReadOnlyConnection();
                preparedStatement = connection.prepareStatement(statement);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.first()) {
                    categoryModel.setTitle(resultSet.getString("home_name"));
                    categoryModel.setUrl(resultSet.getString("home_name_for_url"));
                    categoryModel.setParentId(0);
                    categoryModel.setId(resultSet.getInt("home_id"));

                    breadcrumbBlogModel.setHomecategory(categoryModel);
                }
                breadcrumbBlogModel.setFlagError(0);

            }else if(isCategory == false && flagList==false && !blogUrl.isEmpty()){
                // request landed directly on the blog description page, so calculate default
                // breadcrumb category and sub category using sort order.
                statement="select sc.cat_id,sc.cat_name,sc.cat_url,sc.cat_par,sc.sub_id,sc.sub_name,sc.sub_url,sc.sub_par" +
                    " from blog_post b join blog_cat_map bcm on b.blog_id=bcm.blog_id join (select bc.categories_id cat_id,bc.categories_name cat_name, " +
                    "bc.categories_name_for_url cat_url,bc.parent_id cat_par,bcc.categories_id sub_id,bcc.categories_name sub_name,bcc.categories_name_for_url sub_url," +
                    "bcc.parent_id sub_par from blog_categories bc left join (select categories_id,categories_name,categories_name_for_url,parent_id from blog_categories) " +
                    "as bcc on bcc.parent_id = bc.categories_id where bc.fk_associate_id = "+fkAssociateId+" order by bc.sort_order limit 1) as sc on " +
                    "bcm.categories_id=(case when sc.sub_id is null then sc.cat_id else sc.sub_id end) where b.url='"+blogUrl+"' and b.fk_associate_id = "+fkAssociateId+" and b.status = 1 ";

                connection = Database.INSTANCE.getReadOnlyConnection();
                preparedStatement = connection.prepareStatement(statement);
                logger.debug("preparedstatement of breadcrumb wen sort order : "+preparedStatement);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.first()) {
                    categoryModel.setTitle(resultSet.getString("sc.cat_name"));
                    categoryModel.setUrl(resultSet.getString("sc.cat_url"));
                    categoryModel.setParentId(resultSet.getInt("sc.cat_par"));
                    categoryModel.setId(resultSet.getInt("sc.cat_id"));

                    categoryModel1.setTitle(resultSet.getString("sc.sub_name"));
                    categoryModel1.setUrl(resultSet.getString("sc.sub_url"));
                    categoryModel1.setParentId(resultSet.getInt("sc.sub_par"));
                    categoryModel1.setId(resultSet.getInt("sc.sub_id"));

                    breadcrumbBlogModel.setCategory(categoryModel);
                    breadcrumbBlogModel.setSubcategory(categoryModel1);
                }
                breadcrumbBlogModel.setFlagHome(0);
                breadcrumbBlogModel.setFlagError(0);

            } else if(isCategory==true && !categoryName.isEmpty()){

                // if we got category or sub category name then choose breadcrumb accordingly,
                // be it listing page or description
                categoryModel = getCategoryDetails(fkAssociateId,isCategory,categoryName,"");

                breadcrumbBlogModel.setCategory(categoryModel);
                if(!subCategoryName.isEmpty()){
                    categoryModel = getCategoryDetails(fkAssociateId,isCategory,categoryName,subCategoryName);
                    breadcrumbBlogModel.setSubcategory(categoryModel);
                    // to get sub category info
                }
                breadcrumbBlogModel.setFlagHome(0);
                breadcrumbBlogModel.setFlagError(0);
            }

        }catch (Exception exception){
            logger.debug("error occured while getting breadcrumb ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return breadcrumbBlogModel;
    }
    public CategoryModel getCategoryDetails(int fkAssociateId, boolean isCategory, String categoryName, String subCategoryName){
        Connection connection = null;
        String statement="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement = null;
        CategoryModel categoryModel = new CategoryModel();
        try{
            if(isCategory == false){
                // most priority one
                statement="select categories_id,parent_id,categories_name,categories_name_for_url from blog_categories where" +
                    " fk_associate_id = "+fkAssociateId+" and status = 1 order by sort_order desc limit 1 ";

            }else if(isCategory == true && subCategoryName.isEmpty() && !categoryName.isEmpty()){
                // category specified
                statement=" select categories_id,parent_id,categories_name,categories_name_for_url from blog_categories where fk_associate_id = "+fkAssociateId+
                    " and categories_name_for_url='"+categoryName+"' and status = 1";

            } else{
                // sub-category specified
                statement=" select categories_id,parent_id,categories_name,categories_name_for_url from blog_categories where fk_associate_id = "+fkAssociateId+
                    " and categories_name_for_url='"+subCategoryName+"' and status = 1";
            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedstatement of finding category : "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                categoryModel.setUrl(resultSet.getString("categories_name_for_url"));
                categoryModel.setTitle(resultSet.getString("categories_name"));
                categoryModel.setParentId(resultSet.getInt("parent_id"));
                categoryModel.setId(resultSet.getInt("categories_id"));
            }

        }catch (Exception exception){
            logger.debug("error occured while getting category ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return categoryModel;
    }

}
