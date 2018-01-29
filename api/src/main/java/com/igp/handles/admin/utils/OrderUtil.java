package com.igp.handles.admin.utils;

import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by shanky on 22/1/18.
 */
public class OrderUtil {
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);

    public boolean insertIntoVendorAssignPrice(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="insert into vendor_assign_price (orders_id,fk_associate_id,order_subtotal,shipping,vendor_price , "
                + " products_id,Product_name,Product_code,city,mail_reminder_flag,assign_time,assign_through,assign_by_user , "
                + " shipping_type,delivery_date,delivery_time,flag_eggless,delivery_boy_ID) "
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?, ?, ? ) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,0); // orderID
            preparedStatement.setInt(2,0); // fk_associateId
            preparedStatement.setDouble(3,0.0); //order_subtotal
            preparedStatement.setDouble(4,0.0); // shipping
            preparedStatement.setDouble(5,0.0); // vendor_price
            preparedStatement.setInt(6,0); // products_id
            preparedStatement.setString(7,""); // Product_name
            preparedStatement.setString(8,""); // Product_code
            preparedStatement.setString(9,""); // city
            preparedStatement.setInt(10,0); //  mail_reminder_flag
            preparedStatement.setInt(11,0); // assign_through
            preparedStatement.setString(12,""); // assign_by_user
            preparedStatement.setString(13,""); // shipping_type
            preparedStatement.setString(14,""); // delivery_date
            preparedStatement.setString(15,"");  // delivery_time
            preparedStatement.setInt(16,0); // flag_eggless
            preparedStatement.setInt(17,0); // delivery_boy_ID

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to insert in vendor_assign_price ");
            } else {
                result=true;
            }


        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean updateOrderProduct(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE orders_products set orders_product_status = ? , fk_associate_id = ? , delivery_status = ? where "
                + " orders_id = ? and orders_prodcuts_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,0); // orderID
            preparedStatement.setInt(2,0); // fk_associateId
            preparedStatement.setDouble(3,0.0); //order_subtotal

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update orders_products ");
            } else {
                result=true;
            }


        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean updateTrackorder(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE trackorders set fk_associate_id = ? where orders_id = ? and orders_prodcuts_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,0); // orderID

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update trackorders ");
            } else {
                result=true;
            }


        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public boolean deleteEntryFromVendorAssignPrice(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="DELETE vendor_assign_price where orders_id = ? and products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,0); // orderID

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to delete from vendor_assign_price ");
            } else {
                result=true;
            }


        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }


}
