package com.igp.handles.utils.FileUpload;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.igp.config.Environment;
import com.igp.config.instance.Database;
import com.igp.handles.models.FileUpload.FileUploadModel;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 25/9/17.
 */
public class UploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);
    public FileUploadModel uploadOrderRelatedFile(FormDataMultiPart multiPart,int orderId,int orderProductId,String status) throws IOException
    {
        boolean result=false;
        String fileExtention="";
        String fileNamePrefix="";
        String s3BaseUrl="",s3BucketName="";
        FileUploadModel fileUploadModel=new FileUploadModel();
        List<String> fileLocation=new ArrayList<>();
        try{

            fileNamePrefix+=status+"/"+orderId+"_"+orderProductId+"_";
            Map<String,List<FormDataBodyPart>> bodyParts =multiPart.getFields();
            s3BaseUrl=Environment.getS3baseUrl();
            s3BucketName=Environment.getVendorPanelS3uploadBucketname();
            for(Map.Entry<String,List<FormDataBodyPart>> entry : bodyParts.entrySet()){
                List<FormDataBodyPart> bodyPartsList = entry.getValue();
                for (int i = 0; i < bodyPartsList.size(); i++) {
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPartsList.get(i).getEntity();
                    String [] splittedFileName=bodyPartsList.get(i).getContentDisposition().getFileName().split("\\.");
                    fileExtention=splittedFileName[splittedFileName.length-1];
                    String fileName = fileNamePrefix+System.currentTimeMillis()+"."+fileExtention;
                    result=uploadFileS3(s3BucketName,fileName,bodyPartEntity.getInputStream());
                    if(result){
                        fileLocation.add(s3BaseUrl+"/"+s3BucketName+"/"+fileName);
                        saveUploadedFileInfoToDB(orderId,orderProductId,status,fileName);
                    }
                }
            }
        }catch (Exception exception){
            logger.debug("Exception Occured while uploading file to s3");
        }
        fileUploadModel.setUploadedFilePath(getUploadedfilePathFromVpFileUpload(orderId));
        return fileUploadModel;
    }

    public void saveFile(InputStream file, String name) {
        try {
			/* Change directory path */
            java.nio.file.Path path = FileSystems.getDefault().getPath(name);
			/* Save InputStream as file */
            Files.copy(file, path);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    public boolean uploadFileS3(String bucketName,String fileName,InputStream file) throws IOException
    {
        boolean result=false;
        try
        {
            AWSCredentials credentials = new BasicAWSCredentials(Environment.getS3uploadAccessKey(), Environment.getS3uploadSecretKey());
            AmazonS3 s3client = new AmazonS3Client(credentials);
            com.amazonaws.regions.Region region = com.amazonaws.regions.Region.getRegion(Regions.US_EAST_1);
            s3client.setRegion(region);
            File scratchFile = File.createTempFile("prefix", "suffix");
            FileUtils.copyInputStreamToFile(file, scratchFile);
            PutObjectResult putObjectResult=s3client.putObject(new PutObjectRequest(bucketName,fileName,scratchFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
            result=true;
        }catch (Exception exception){
            logger.debug("Exception Occured while uploading file to s3");
        }
        return result;
    }
    public boolean saveUploadedFileInfoToDB(int orderId,int orderProductId,String status,String filePath){
        Connection connection = null;
        String statement;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "insert into vp_file_upload (orders_products_id,orders_id,type,file_path) values (?,?,?,?)";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderProductId);
            preparedStatement.setInt(2,orderId);
            if(status.equalsIgnoreCase("OutForDelivery")){
                preparedStatement.setInt(3,0);
            }else if(status.equalsIgnoreCase("Delivered")){
                preparedStatement.setInt(3,1);
            }
             preparedStatement.setString(4,filePath);
            logger.debug("sql query "+preparedStatement);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;
            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;
    }

    public boolean deleteFile(int orderId,int orderProductId,String filePath){
        boolean result=false;
        String fileName="",s3BucketName="";// filePath is like https://s3.amazonaws.com/vendorimageupload/OutForDelivery/111115_222226_1506583474381.jpg
        String[] intermediatePaths=filePath.split("/");
        s3BucketName=Environment.getVendorPanelS3uploadBucketname();
        fileName=intermediatePaths[intermediatePaths.length-2]+"/"+intermediatePaths[intermediatePaths.length-1];// fileName is like OutForDelivery/111115_222226_1506583474381.jpg
        result=deleteFileFromS3(s3BucketName,fileName);
        if(result){
            result=deleteEntryFromDBAfterDelete(orderId,orderProductId,fileName);
        }
        return result;
    }

    public boolean deleteFileFromS3(String bucketName,String fileName){
        boolean result=false;
        try
        {
            AWSCredentials credentials = new BasicAWSCredentials(Environment.getS3uploadAccessKey(), Environment.getS3uploadSecretKey());
            AmazonS3 s3client = new AmazonS3Client(credentials);
            com.amazonaws.regions.Region region = com.amazonaws.regions.Region.getRegion(Regions.US_EAST_1);
            s3client.setRegion(region);
            s3client.deleteObject(bucketName,fileName);
            result=true;
        }catch (Exception exception){
            logger.debug("Exception Occured while deleting file from s3");
        }
        return result;
    }
    public boolean deleteEntryFromDBAfterDelete(int orderId,int orderProductId,String filePath){
        Connection connection = null;
        String statement;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "DELETE FROM vp_file_upload WHERE orders_products_id = ? AND orders_id = ? AND file_path = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderProductId);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setString(3,filePath);
            logger.debug("sql query "+preparedStatement);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;
            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;
    }
    public Map<String, List<String>> getUploadedfilePathFromVpFileUpload(int orderId){
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        String s3BaseUrl="",s3BucketName="";
        List<String> fileLocationType0=new ArrayList<>(); //type 0 = outfordelivery
        List<String> fileLocationType1=new ArrayList<>(); //type 1 = delivered
        Map<String, List<String>> uploadedFilePathMap=new HashMap<>();
        try
        {
            s3BaseUrl=Environment.getS3baseUrl();
            s3BucketName=Environment.getVendorPanelS3uploadBucketname();
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select * from vp_file_upload where orders_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getInt("type")==0){
                    fileLocationType0.add(s3BaseUrl+"/"+s3BucketName+"/"+resultSet.getString("file_path"));
                }else if(resultSet.getInt("type")==1) {
                    fileLocationType1.add(s3BaseUrl+"/"+s3BucketName+"/"+resultSet.getString("file_path"));
                }

            }
            uploadedFilePathMap.put("OutForDelivery",fileLocationType0);
            uploadedFilePathMap.put("Delivered",fileLocationType1);
        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return uploadedFilePathMap;
    }
}
