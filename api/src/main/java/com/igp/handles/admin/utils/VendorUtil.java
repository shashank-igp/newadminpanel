package com.igp.handles.admin.utils;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.VendorInfoModel;
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
}
