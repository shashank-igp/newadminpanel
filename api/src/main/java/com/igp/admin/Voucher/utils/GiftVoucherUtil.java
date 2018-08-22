package com.igp.admin.Voucher.utils;

import com.igp.admin.Voucher.models.GiftVoucherListModel;
import com.igp.admin.Voucher.models.GiftVoucherModel;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikhil on 21/08/18.
 */
public class GiftVoucherUtil
{
    private static final Logger logger = LoggerFactory.getLogger(GiftVoucherUtil.class);

    public static boolean createGiftVoucher(GiftVoucherModel giftVoucherModel){
        boolean retVal=false;
        logger.debug("INSERTING INTO GIFT_VOUCHERS TABLE");
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try{

            connection = Database.INSTANCE.getReadWriteConnection();
            connection.setAutoCommit(false);
            statement = "INSERT IGNORE INTO gift_vouchers(coupon_code,coupon_cost,coupon_cost_dollor,coupon_uses,coupon_link,coupon_status,expDate,description,affiliate_id,voucherCat) VALUES (?,?,?,?,?,?,?,?,?,?)" ;

            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,giftVoucherModel.getCouponCode());
            preparedStatement.setBigDecimal(2,giftVoucherModel.getCouponcost());
            preparedStatement.setBigDecimal(3,giftVoucherModel.getCouponDostDollar());
            preparedStatement.setInt(4,0);  //coupon_code
            preparedStatement.setString(5,giftVoucherModel.getCouponLink());
            preparedStatement.setString(6,"Y");
            Date date = dateFormat.parse(giftVoucherModel.getExpiryDate());
            preparedStatement.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
            preparedStatement.setString(8,giftVoucherModel.getDescription());
            preparedStatement.setInt(9,giftVoucherModel.getFkAssociateId());
            preparedStatement.setString(10,giftVoucherModel.getVoucherCat());
            logger.debug("Statement : "+ preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status != 0) {
                retVal = true;
            }
            if(retVal == true) {
                connection.commit();
            } else {
                connection.rollback();
                logger.error("Exception in creating gift voucher");
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


    public boolean updateGiftVoucher(GiftVoucherModel giftVoucherModel){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        boolean result = false;

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            connection = Database.INSTANCE.getReadWriteConnection();
            connection.setAutoCommit(false);
            statement="UPDATE gift_vouchers SET coupon_code=?,coupon_cost=?,coupon_cost_dollor=?,coupon_link=?," +
                "coupon_status=?,expDate=?,description=?,affiliate_id=? WHERE cid = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,giftVoucherModel.getCouponCode());
            preparedStatement.setBigDecimal(2,giftVoucherModel.getCouponcost());
            preparedStatement.setBigDecimal(3,giftVoucherModel.getCouponDostDollar());
            preparedStatement.setString(4,giftVoucherModel.getCouponLink());
            preparedStatement.setString(5,giftVoucherModel.getCouponStatus());
            Date date = dateFormat.parse(giftVoucherModel.getExpiryDate());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(date.getTime()));
            preparedStatement.setString(7,giftVoucherModel.getDescription() );
            preparedStatement.setInt(8,giftVoucherModel.getFkAssociateId());
            preparedStatement.setInt(9,giftVoucherModel.getId());

            logger.debug("preparedstatement of update gift_vouchers : "+preparedStatement);
            int  status = preparedStatement.executeUpdate();

            if(status==1){
                result=true;
                connection.commit();
            } else {
                connection.rollback();
                logger.error("Exception in updating gift voucher");
            }

        }catch (Exception exception){
            logger.debug("error occured while updating gift voucher: "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public boolean deleteGiftVoucher(int id, int fkAssociateId){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE gift_vouchers SET coupon_status ='N' WHERE cid = ? AND affiliate_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, fkAssociateId);
            logger.debug("preparedstatement of delete Voucher_post : "+preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status != 0){
                result = true;
                logger.debug("voucher disabled from gift voucher with id : "+id);
            }

        }catch (Exception exception){
            logger.debug("error occured while disabling gift voucher "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public boolean validateGiftVoucher(int fkAssociateId, String voucherCode){
        boolean result = false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="SELECT cid FROM gift_vouchers WHERE coupon_code = ? and affiliate_id=?";
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

    public GiftVoucherListModel getGiftVoucherList(int id, int fkAssociateId, int start, int end){
        Connection connection = null;
        String statement,condition="";
        ResultSet resultSet =  null;
        PreparedStatement preparedStatement=null;
        List<GiftVoucherModel> giftVoucherModelList = new ArrayList<>();
        GiftVoucherListModel giftVoucherListModel = new GiftVoucherListModel();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            if(id != -1){
                condition = " where cid= "+id;
            }else if(fkAssociateId != -1){
                condition = " where affiliate_id= "+fkAssociateId;
            }

            String queryTotal="select count(*) as totalno from gift_vouchers "+condition;
            preparedStatement = connection.prepareStatement(queryTotal);
            logger.debug("preparedstatement of gift_voucher list : "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.first()){
                int count = resultSet.getInt("totalno");
                giftVoucherListModel.setCount(count);
            }

            statement = "SELECT * from gift_vouchers "+condition+" order by cid DESC limit ?,?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,start);
            preparedStatement.setInt(2,end);
            logger.debug("preparedstatement of gift voucher list : "+preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try{
                GiftVoucherModel giftVoucherModel = new GiftVoucherModel();

                //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    giftVoucherModel.setId(resultSet.getInt("cid"));
                    giftVoucherModel.setCouponCode(resultSet.getString("coupon_code"));
                    giftVoucherModel.setCouponcost(resultSet.getBigDecimal("coupon_cost"));
                    giftVoucherModel.setCouponDostDollar(resultSet.getBigDecimal("coupon_cost_dollor"));
                    giftVoucherModel.setCouponUses(resultSet.getInt("coupon_uses"));
                    giftVoucherModel.setCouponLink(resultSet.getString("coupon_link"));
                    giftVoucherModel.setCouponStatus(resultSet.getString("coupon_status"));
                    giftVoucherModel.setPurchaseOrderId(resultSet.getString("purchase_orderID"));
                    giftVoucherModel.setUsedOrderId(resultSet.getString("used_orderID"));
                    giftVoucherModel.setVoucherCat(resultSet.getString("voucherCat"));
                    //giftVoucherModel.setExpiryDate(resultSet.getString("expDate"));
                    try
                    {
                        giftVoucherModel.setExpiryDate(dateFormat.format(resultSet.getTimestamp("expDate")));
                    }catch (java.sql.SQLException ex){
                        logger.error(ex.getMessage());
                        giftVoucherModel.setExpiryDate("0000-00-00");
                    }

                    giftVoucherModel.setDescription(resultSet.getString("description"));
                    giftVoucherModel.setFkAssociateId(resultSet.getInt("affiliate_id"));

                    giftVoucherModelList.add(giftVoucherModel);
                }catch (Exception exception){
                    logger.debug("error occured while getting gift voucher list : "+exception);
                }
            }
            giftVoucherListModel.setGiftVoucherModelList(giftVoucherModelList);

        }catch (Exception exception){
            logger.debug("error occured while getting voucher list : "+exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return giftVoucherListModel ;
    }

}
