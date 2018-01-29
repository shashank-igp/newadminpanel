package com.igp.handles.admin.utils.Vendor;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.Vendor.VendorAssignModel;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 26/1/18.
 */
public class VendorUtil
{
    private static final Logger logger = LoggerFactory.getLogger(VendorUtil.class);

    public List<VendorInfoModel> getVendorList(int pincode,int shippingType){
        List<VendorInfoModel> vendorInfoModelList=new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "select * from associate a join vendor_extra_info v on a.associate_id=v.associate_id join "
                + " AA_vendor_pincode  avp on avp.vendor_id = a.associate_id where a.associate_status =1 and v.type = 2 and "
                + " avp.flag_enabled = 1 and avp.pincode = ? and avp.ship_type = ? GROUP  by avp.vendor_id ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,pincode);
            preparedStatement.setInt(2,shippingType);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                VendorInfoModel vendorInfoModel=new VendorInfoModel();
                vendorInfoModel.setVendorId(resultSet.getInt("a.associate_id"));
                vendorInfoModel.setAssociateName(resultSet.getString("a.associate_name"));
                vendorInfoModelList.add(vendorInfoModel);
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return vendorInfoModelList;
    }

    public boolean insertIntoVendorAssignPrice(VendorAssignModel vendorAssignModel){
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
            preparedStatement.setInt(1,vendorAssignModel.getOrderId()); // orderID
            preparedStatement.setInt(2,vendorAssignModel.getFkAssociateId()); // fk_associateId
            preparedStatement.setDouble(3,vendorAssignModel.getOrderSubtotal()); //order_subtotal
            preparedStatement.setDouble(4,vendorAssignModel.getShipping()); // shipping
            preparedStatement.setDouble(5,vendorAssignModel.getVendorPrice()); // vendor_price
            preparedStatement.setInt(6,vendorAssignModel.getProductId()); // products_id
            preparedStatement.setString(7,vendorAssignModel.getProductName()); // Product_name
            preparedStatement.setString(8,vendorAssignModel.getProductCode()); // Product_code
            preparedStatement.setString(9,vendorAssignModel.getCity()); // city
            preparedStatement.setInt(10,vendorAssignModel.getMailRemainderFlag()); //  mail_reminder_flag
            preparedStatement.setInt(11,vendorAssignModel.getAssignThrough()); // assign_through
            preparedStatement.setString(12,vendorAssignModel.getAssignByUser()); // assign_by_user
            preparedStatement.setString(13,vendorAssignModel.getShipppingType()); // shipping_type
            preparedStatement.setString(14,vendorAssignModel.getDeliveryDate()); // delivery_date
            preparedStatement.setString(15,vendorAssignModel.getDeliveryTime());  // delivery_time
            preparedStatement.setInt(16,vendorAssignModel.getFlagEggless()); // flag_eggless
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
}
