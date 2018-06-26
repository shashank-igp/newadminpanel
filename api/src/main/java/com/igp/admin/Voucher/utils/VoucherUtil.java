package com.igp.admin.Voucher.utils;

import com.igp.admin.Blogs.models.SeoBlogModel;
import com.igp.admin.Voucher.models.VoucherListModel;
import com.igp.admin.Voucher.models.VoucherMetaData;
import com.igp.admin.Voucher.models.VoucherModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by suditi on 8/6/18.
 */
public class VoucherUtil {
    private static final Logger logger = LoggerFactory.getLogger(VoucherUtil.class);

    public static boolean createVoucher(VoucherModel voucherModel){
        boolean retVal=false;
        int autoGeneratedID;
        logger.debug("INSERTING INTO VOUCHER TABLE AND NEWIGP_VOUCHER_EXTRA_INFO");
        Connection connection = null;
        String statement;
        String statement2;
        PreparedStatement preparedStatement = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try{
            if((voucherModel.getApplicableVoucherType()==1 || voucherModel.getApplicableVoucherType()==2) && voucherModel.getApplicableEmail()!=null){
                // email & domain specific
                String voucher = generateVoucherCodeFromCustomerName(voucherModel.getApplicableEmail().get(0));
                voucherModel.setVoucherCode(voucher);
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            connection.setAutoCommit(false);
            statement = "INSERT IGNORE INTO voucher(voucher_code,expiry_date,discount,affiliate_id," +
                "comment,multiple_usage,enabled,used_count,applicable_category,coupon_type,gen_date) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,now())   " ;// "ON DUPLICATE KEY UPDATE expiry_date= (now() + INTERVAL 1 MONTH) , gen_date= (now()) ";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,voucherModel.getVoucherCode()); //voucher code
            Date date = dateFormat.parse(voucherModel.getExpiryDate());
            preparedStatement.setDate(2, new java.sql.Date(date.getTime())); // expiry date
            preparedStatement.setInt(3,voucherModel.getVoucherValue());   // discount percentage
            preparedStatement.setInt(4,voucherModel.getFkAssociateId());  // affiliate
            preparedStatement.setString(5,voucherModel.getComment());  // comment
            preparedStatement.setInt(6,voucherModel.getMultipleUsage()); //  multiple usage
            preparedStatement.setInt(7,voucherModel.getEnabled());  // enabled
            preparedStatement.setInt(8,voucherModel.getUsedCount() );  // used_count
            preparedStatement.setString(9,voucherModel.getApplicableCategory()); // applicable category
            preparedStatement.setInt(10,0); // coupon type is voucher
            logger.debug("Statement :"+ preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status != 0) {

                String blackList = "";
                String whiteList = "";
                String email = "";
                if(voucherModel.getBlackListPts()!=null && !voucherModel.getBlackListPts().isEmpty()){
                    int i = voucherModel.getBlackListPts().size()-1;
                    while(i>=0){
                        blackList+=voucherModel.getBlackListPts().get(i)+",";
                        i--;
                    }
                    blackList = blackList.substring(0, blackList.length() - 1);
                }
                if(voucherModel.getWhiteListPts()!=null && !voucherModel.getWhiteListPts().isEmpty()){
                    int i = voucherModel.getWhiteListPts().size()-1;
                    while(i>=0){
                        whiteList+=voucherModel.getWhiteListPts().get(i)+",";
                        i--;
                    }
                    whiteList = whiteList.substring(0, whiteList.length() - 1);
                }
                if(voucherModel.getApplicableEmail()!=null && !voucherModel.getApplicableEmail().isEmpty()){
                    int i = voucherModel.getApplicableEmail().size()-1;
                    while(i>=0){
                        email+=voucherModel.getApplicableEmail().get(i)+",";
                        i--;
                    }
                    email = email.substring(0, email.length() - 1);
                }
                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
                autoGeneratedID = tableKeys.getInt(1);
                logger.debug("Created voucher with ID = "+autoGeneratedID+"  ");
                statement2 ="INSERT INTO newigp_voucher_extra_info(fk_voucher_id,coupon_type,email," +
                    "total_order_value,black_list_pt,white_list_pt,shipping_waiver_type," +
                    "order_value_check,applicable_pid,product_quant,created_by) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
                preparedStatement=connection.prepareStatement(statement2);
                preparedStatement.setInt(1,autoGeneratedID); // id auto generated in voucher table
                preparedStatement.setInt(2,voucherModel.getApplicableVoucherType()); // coupon type : 0-noEmail, 1-emailList, 2-domainBased
                preparedStatement.setString(3,email);
                preparedStatement.setInt(4,voucherModel.getOrderValue());
                preparedStatement.setString(5,blackList);
                preparedStatement.setString(6,whiteList);
                preparedStatement.setInt(7,voucherModel.getShippingWaiverType());
                preparedStatement.setInt(8,voucherModel.getOrderValueCheck());
                preparedStatement.setString(9,voucherModel.getApplicablePid());
                preparedStatement.setString(10,voucherModel.getProductQuant());
                preparedStatement.setString(11,voucherModel.getCreatedBy());
                logger.debug("Statement :"+ preparedStatement);
                Integer status2 = preparedStatement.executeUpdate();
                if(status2==1) {
                    retVal = true;
                }
            }
            if(retVal == true) {
                connection.commit();
            } else {
                connection.rollback();
                logger.error("Exception in creating voucher2");
            }

        }
        catch (Exception exception){
            logger.error("Exception in connection", exception);
        }
        finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return retVal;
    }


    public boolean updateVoucher(VoucherModel voucherModel){
        Connection connection = null;
        String statement,statement2;
        PreparedStatement preparedStatement = null;
        boolean result = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String blackList = "";
            String whiteList = "";
            String email = "";
            if(voucherModel.getBlackListPts()!=null && !voucherModel.getBlackListPts().isEmpty()){
                int i = voucherModel.getBlackListPts().size()-1;
                while(i>=0){
                    blackList+=voucherModel.getBlackListPts().get(i)+",";
                    i--;
                }
                blackList = blackList.substring(0, blackList.length() - 1);
            }
            if(voucherModel.getWhiteListPts()!=null && !voucherModel.getWhiteListPts().isEmpty()){
                int i = voucherModel.getWhiteListPts().size()-1;
                while(i>=0){
                    whiteList+=voucherModel.getWhiteListPts().get(i)+",";
                    i--;
                }
                whiteList = whiteList.substring(0, whiteList.length() - 1);
            }
            if(voucherModel.getApplicableEmail()!=null && !voucherModel.getApplicableEmail().isEmpty()){
                int i = voucherModel.getApplicableEmail().size()-1;
                while(i>=0){
                    email+=voucherModel.getApplicableEmail().get(i)+",";
                    i--;
                }
                email = email.substring(0, email.length() - 1);
            }

            connection = Database.INSTANCE.getReadWriteConnection();
            connection.setAutoCommit(false);
            statement="UPDATE voucher SET voucher_code=?,expiry_date=?,discount=?,affiliate_id=?," +
                "comment=?,multiple_usage=?,enabled=?,used_count=?,applicable_category=?,coupon_type=? WHERE id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,voucherModel.getVoucherCode()); //voucher code
            Date date = dateFormat.parse(voucherModel.getExpiryDate());
            preparedStatement.setDate(2, new java.sql.Date(date.getTime()));//expiry date
            preparedStatement.setInt(3,voucherModel.getVoucherValue());   // discount percentage
            preparedStatement.setInt(4,voucherModel.getFkAssociateId());  // affiliate
            preparedStatement.setString(5,voucherModel.getComment());  // comment
            preparedStatement.setInt(6,voucherModel.getMultipleUsage()); //  multiple usage
            preparedStatement.setInt(7,voucherModel.getEnabled());  // enabled
            preparedStatement.setInt(8,voucherModel.getUsedCount() );  // used_count
            preparedStatement.setString(9,voucherModel.getApplicableCategory()); // applicable category
            preparedStatement.setInt(10,voucherModel.getVoucherType()); // coupon type corporate
            preparedStatement.setInt(11,voucherModel.getId());

            logger.debug("preparedstatement of update Voucher_post : "+preparedStatement);
            int  status = preparedStatement.executeUpdate();
            statement2 ="UPDATE newigp_voucher_extra_info SET coupon_type=?,email=?," +
                "total_order_value=?,black_list_pt=?,white_list_pt=?,shipping_waiver_type=?," +
                "order_value_check=?,applicable_pid=?,product_quant=?, modified_by=?, date_modified=now() where fk_voucher_id=? ";
            preparedStatement=connection.prepareStatement(statement2);
            preparedStatement.setInt(1,voucherModel.getApplicableVoucherType()); // coupon type : 0-noEmail, 1-emailList, 2-domainBased
            preparedStatement.setString(2,email);
            preparedStatement.setInt(3,voucherModel.getOrderValue());
            preparedStatement.setString(4,blackList);
            preparedStatement.setString(5,whiteList);
            preparedStatement.setInt(6,voucherModel.getShippingWaiverType());
            preparedStatement.setInt(7,voucherModel.getOrderValueCheck());
            preparedStatement.setString(8,voucherModel.getApplicablePid());
            preparedStatement.setString(9,voucherModel.getProductQuant());
            preparedStatement.setString(10,voucherModel.getModifiedBy());
            preparedStatement.setInt(11,voucherModel.getId());

            logger.debug("Statement :"+ preparedStatement);
            Integer status2 = preparedStatement.executeUpdate();
            if(status==1 || status2==1){
                result=true;
                connection.commit();
            } else {
                connection.rollback();
                logger.error("Exception in updating voucher2");
            }

        }catch (Exception exception){
            logger.debug("error occured while updating Voucher: "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean deleteVoucher(int id, String modifiedBy){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE voucher v JOIN newigp_voucher_extra_info nv ON " +
                " v.id = nv.fk_voucher_id SET enabled =0, modified_by=? WHERE id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,modifiedBy);
            preparedStatement.setInt(2, id);
            logger.debug("preparedstatement of delete Voucher_post : "+preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status != 0){
                result = true;
                logger.debug("voucher disabled from voucher with id : "+id);
            }

        }catch (Exception exception){
            logger.debug("error occured while disabling Voucher "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public boolean validateVoucher(int fkAssociateId, String voucherCode){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="SELECT id FROM voucher WHERE voucher_code = ? and affiliate_id=?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,voucherCode);
            preparedStatement.setInt(2, fkAssociateId);
            logger.debug("preparedstatement of validate Voucher : "+preparedStatement);
           resultSet = preparedStatement.executeQuery();
            if (resultSet.first()){
                result = true;
                logger.debug("voucher code already exists : "+voucherCode);
            }

        }catch (Exception exception){
            logger.debug("error occured while disabling Voucher "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return result;
    }


    public static String generateVoucherCodeFromCustomerName(String email){
        String firstHalfVoucher="",lastHalfVoucher="",random="";
        int lastHalfVoucherLength=0;
        if(email.length()>=4){
            firstHalfVoucher=email.substring(0,4);
        }
        else{
            firstHalfVoucher=email;
        }
        java.util.Date date = new java.util.Date();
        lastHalfVoucher = Long.toString(date.getTime());
        lastHalfVoucherLength=lastHalfVoucher.length();
        if(lastHalfVoucherLength >= 3 ){

            lastHalfVoucher=lastHalfVoucher.substring(lastHalfVoucherLength-3, lastHalfVoucherLength);
        }
        else if(lastHalfVoucherLength == 2 ){
            random="0";
            lastHalfVoucher=random+lastHalfVoucher;
        }
        else if(lastHalfVoucherLength == 1 ){
            random="00";
            lastHalfVoucher=random+lastHalfVoucher;
        }
        return  firstHalfVoucher+lastHalfVoucher;
    }
    public VoucherListModel getVoucherList(int id, int fkAssociateId, int start, int end){
        Connection connection = null;
        String statement,condition="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement=null;
        List<VoucherModel> voucherModelList = new ArrayList<>();
        VoucherListModel voucherListModel = new VoucherListModel();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            if(id != -1){
                condition = " where v.id= "+id;
            }else if(fkAssociateId != -1){
                condition = " where v.affiliate_id= "+fkAssociateId;
            }

            String queryTotal="select count(*) as totalno from voucher v JOIN newigp_voucher_extra_info nv ON v.id = nv.fk_voucher_id "+condition;
            preparedStatement = connection.prepareStatement(queryTotal);
            logger.debug("preparedstatement of voucher list : "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.first()){
                int count = resultSet.getInt("totalno");
                voucherListModel.setCount(count);
            }

            statement = "SELECT * from voucher v JOIN newigp_voucher_extra_info nv ON" +
                " v.id = nv.fk_voucher_id "+condition+" order by v.id DESC limit ?,?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,start);
            preparedStatement.setInt(2,end);
            logger.debug("preparedstatement of voucher list : "+preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try{
                VoucherModel voucherModel = new VoucherModel();
                String blackList = resultSet.getString("nv.black_list_pt");
                String whiteList = resultSet.getString("nv.white_list_pt");
                List<Integer> blackarray = new ArrayList<>();
                List<Integer> whitearray = new ArrayList<>();
                if(blackList!=null && !blackList.isEmpty()){
                    String split[] = blackList.split(",");
                    int i=split.length-1;
                    while(i>0){
                        blackarray.add(new Integer(split[i]));
                        i--;
                    }
                    blackarray.add(new Integer(split[i]));
                }
                if(whiteList!=null && !whiteList.isEmpty()){
                    String split[] = whiteList.split(",");
                    int i=split.length-1;
                    while(i>0){
                        whitearray.add(new Integer(split[i]));
                        i--;
                    }
                    whitearray.add(new Integer(split[i]));
                }
                String emailList = resultSet.getString("nv.email");
                List<String> emailarray = new ArrayList<>();
                if(emailList!=null && !emailList.isEmpty()){
                    String split[] = emailList.split(",");
                    int i=split.length-1;
                    while(i>0){
                        emailarray.add(split[i]);
                        i--;
                    }
                    emailarray.add(split[i]);
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                    voucherModel.setId(resultSet.getInt("v.id"));
                    voucherModel.setVoucherCode(resultSet.getString("v.voucher_code"));
                    voucherModel.setVoucherValue(resultSet.getInt("v.discount"));
                    voucherModel.setFkAssociateId(resultSet.getInt("v.affiliate_id"));
                    voucherModel.setComment(resultSet.getString("v.comment"));
                    voucherModel.setMultipleUsage(resultSet.getInt("v.multiple_usage"));
                    voucherModel.setEnabled(resultSet.getInt("v.enabled"));
                    voucherModel.setUsedCount(resultSet.getInt("v.used_count"));
                    voucherModel.setApplicableCategory(resultSet.getString("v.applicable_category"));
                    voucherModel.setVoucherType(resultSet.getInt("v.coupon_type"));
                    voucherModel.setApplicableVoucherType(resultSet.getInt("nv.coupon_type"));
                    voucherModel.setApplicableEmail(emailarray);
                    voucherModel.setOrderValue(resultSet.getInt("nv.total_order_value"));
                    voucherModel.setOrderValueCheck(resultSet.getInt("nv.order_value_check"));
                    voucherModel.setShippingWaiverType(resultSet.getInt("nv.shipping_waiver_type"));
                    voucherModel.setProductQuant(resultSet.getString("nv.product_quant"));
                    voucherModel.setApplicablePid(resultSet.getString("nv.applicable_pid"));
                    voucherModel.setCreatedBy(resultSet.getString("nv.created_by"));
                    voucherModel.setModifiedBy(resultSet.getString("nv.modified_by"));
                    voucherModel.setExpiryDate(dateFormat.format( resultSet.getTimestamp("v.expiry_date")));
                    voucherModel.setCreatedDate(dateFormat.format( resultSet.getTimestamp("v.gen_date")));
                    voucherModel.setModifiedDate(dateFormat.format( resultSet.getTimestamp("nv.date_modified")));
                    voucherModel.setBlackListPts(blackarray);
                    voucherModel.setWhiteListPts(whitearray);
                    // date created,expiry
                    voucherModelList.add(voucherModel);
                }catch (Exception exception){
                    logger.debug("error occured while getting voucher list : "+exception);
                }
            }
            voucherListModel.setVoucherModelList(voucherModelList);

        }catch (Exception exception){
            logger.debug("error occured while getting voucher list : "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return voucherListModel;
    }

    public List<String> getExistingBlackListProdCats(){
        Connection connection = null;
        String statement;
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement=null;
        List<String> blackList = new ArrayList<>();

        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select black_list_pt from newigp_voucher_extra_info where fk_voucher_id=3395 ";//FAMILY40
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("preparedStatement for getExistingBlackListProdCats => "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                blackList = Arrays.asList(resultSet.getString("black_list_pt").split(","));
            }

        }catch (Exception exception){
            logger.debug("error occured while getting voucher list : "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return blackList;
    }

    public List<SeoBlogModel> getAssociates(){
        Connection connection = null;
        String statement;
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement=null;
        List<SeoBlogModel> associateList = new ArrayList<>();

        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select * from associate where associate_id in (5,830) ";
            preparedStatement = connection.prepareStatement(statement);
           // Array array = connection.createArrayOf("INTEGER", new Object[]{5,830});
           // preparedStatement.setArray(1,array);

            logger.debug("preparedStatement for getAssociates => "+preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                SeoBlogModel seoBlogModel = new SeoBlogModel();
                seoBlogModel.setFkAssociateId(resultSet.getInt("associate_id"));
                seoBlogModel.setSeoTitle(resultSet.getString("associate_name"));
                seoBlogModel.setStoreName(resultSet.getString("associate_login_id"));
                associateList.add(seoBlogModel);
            }

        }catch (Exception exception){
            logger.debug("error occured while getting associates list : "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return associateList;
    }

    public VoucherMetaData getVoucherMetaData(){
        VoucherMetaData voucherMetaData = new VoucherMetaData();
        Map<Integer, String> types = new HashMap<>();

        try{
            //coupon type voucher
            types.put(0,"% based");
            types.put(1,"Value based");
            types.put(2,"Shipping waiver");
            types.put(3,"Corporate gifting voucher");
            voucherMetaData.setType1(types);

            types = new HashMap<>();
            //coupon type newigp_voucher_extra_info
            types.put(0,"No email");
            types.put(1,"Email based");
            types.put(2,"Domain based");
            voucherMetaData.setType2(types);

            types = new HashMap<>();
            //order value check newigp_voucher_extra_info
            types.put(0,"No check");
            types.put(1,"Order level without shipping");
            types.put(2,"Order level with shipping");
            types.put(3,"Product level");
            voucherMetaData.setOrderValueCheck(types);

            types = new HashMap<>();
            //shipping waiver type newigp_voucher_extra_info
            types.put(1,"Standard Delivery");
            types.put(2,"Fixed Time Delivery");
            types.put(3,"Midnight Delivery");
            types.put(4,"Fix Date Delivery");
            voucherMetaData.setShippingWaiverType(types);

        }catch (Exception exception){
            logger.debug("error occured while getting voucher meta data : "+exception);
        }
        return voucherMetaData;
    }

}
