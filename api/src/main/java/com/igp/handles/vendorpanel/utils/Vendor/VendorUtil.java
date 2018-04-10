package com.igp.handles.vendorpanel.utils.Vendor;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.Order.OrderProductExtraInfo;
import com.igp.handles.vendorpanel.models.Order.OrdersProducts;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import com.igp.handles.vendorpanel.models.Vendor.VendorInstruction;
import com.igp.handles.vendorpanel.utils.Order.OrderStatusUpdateUtil;
import com.igp.handles.vendorpanel.utils.Order.OrderUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanky on 8/7/17.
 */
public class VendorUtil
{

    private static final Logger logger = LoggerFactory.getLogger(VendorUtil.class);

    public List<OrderDetailsPerOrderProduct> getOrderListForVendor(String fkAssociateId, Date date,int flag){
        //flag=0  vap.delivery_date>=date , include Shipped    , flag=1 vap.delivery_date==date , exclude Shipped
        Connection connection = null;
        String statement,pStatus= flag==0 ? ",'Shipped'":""  , dateComapareSymbol= flag==0 ? ">=":"=";
        ResultSet resultSet = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement preparedStatement = null;

        List<OrderDetailsPerOrderProduct> listOfOrderIdAsPerVendor=new ArrayList<>();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select torder.orders_id, ( CASE WHEN torder.deliveredDate!='0000-00-00 00:00:00' THEN torder.deliveredDate END ) as deliveredDate,"
                + "vap.delivery_date ,op.orders_product_status,op.delivery_status  ,vap.delivery_time , vap.shipping_type ,op.sla_code ,"
                + "( CASE WHEN torder.outForDeliveryDate !='0000-00-00 00:00:00' THEN torder.outForDeliveryDate END ) as  outForDeliveryDate,op.orders_products_id  "
                + " , op.delivery_attempt,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN vap.assign_time "
                + " END ) as  vendor_assign_time from orders_products op join order_product_extra_info opei on "
                + " op.orders_products_id = opei.order_product_id join vendor_assign_price vap on vap.orders_id = op.orders_id "
                + " and vap.products_id = op.products_id left join trackorders torder on op.orders_id = torder.orders_id "
                + " and op.orders_products_id = torder.orders_products_id where  op.orders_product_status "
                + " in ('Processed','Confirmed'" +pStatus + ") and  vap.fk_associate_id = ?  and "
                + " vap.delivery_date "+dateComapareSymbol+" ? and vap.shipping_type !='Any time'  and  vap.shipping_type !='' ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, fkAssociateId);
            preparedStatement.setString(2,new SimpleDateFormat("yyyy-MM-dd").format(date));
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date deliveredDate=resultSet.getString("deliveredDate")== null ? null : DateUtils.truncate(dateFormat.parse(resultSet.getString("deliveredDate")), Calendar.DAY_OF_MONTH);
                Date outOfDeliryDate=resultSet.getString("outForDeliveryDate")== null ? null : DateUtils.truncate(dateFormat.parse(resultSet.getString("outForDeliveryDate")), Calendar.DAY_OF_MONTH);
                OrderDetailsPerOrderProduct orderDetailsPerOrderProduct =new OrderDetailsPerOrderProduct();
                orderDetailsPerOrderProduct.setOrdersId(resultSet.getLong("torder.orders_id"));
                orderDetailsPerOrderProduct.setDeliveredDate(deliveredDate);
                orderDetailsPerOrderProduct.setDeliveryDate(DateUtils.truncate(dateFormat.parse(resultSet.getString("vap.delivery_date")), Calendar.DAY_OF_MONTH));
                orderDetailsPerOrderProduct.setOutForDeliveryDate(outOfDeliryDate);
                orderDetailsPerOrderProduct.setOrderProductStatus(resultSet.getString("op.orders_product_status"));
                orderDetailsPerOrderProduct.setDeliveryStatus(resultSet.getBoolean("op.delivery_status"));
//                orderDetailsPerOrderProduct.setDeliveryTime(resultSet.getString("vap.delivery_time"));
//                orderDetailsPerOrderProduct.setShippingType(resultSet.getString("vap.shipping_type"));
                orderDetailsPerOrderProduct.setSlaCode(resultSet.getInt("op.sla_code"));
                orderDetailsPerOrderProduct.setOrdersProductsId(resultSet.getLong("op.orders_products_id"));
                orderDetailsPerOrderProduct.setDeliveryAttemptFlag(resultSet.getInt("op.delivery_attempt"));

                orderDetailsPerOrderProduct.setAssignTime(resultSet.getString("vendor_assign_time"));
                orderDetailsPerOrderProduct.setShippingType(resultSet.getString("opei.delivery_type"));
                orderDetailsPerOrderProduct.setDeliveryTime(resultSet.getString("opei.delivery_time"));
                orderDetailsPerOrderProduct.setVendorId(resultSet.getInt("op.fk_associate_id"));
                listOfOrderIdAsPerVendor.add(orderDetailsPerOrderProduct);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return listOfOrderIdAsPerVendor;
    }
    public Date getFestivalDate(Date todayDate){
        Date festivalDate=null;
        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT min(festival_date) as festival_date from igp_festival_date where festival_date >= ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,dateFormat.format(todayDate));
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                festivalDate=resultSet.getString("festival_date")== null ? todayDate : DateUtils.truncate(dateFormat.parse(resultSet.getString("festival_date")), Calendar.DAY_OF_MONTH);
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return festivalDate;
    }
    public boolean saveVendorIssueInHandelsHistory(String orderProductIds,int orderId,String fkAssociateId,String vendorIssue,String ipAddress,String userAgent){
        boolean result=false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        int rejectionType=9; // for alternate number/address issue
        Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap=new HashMap<>();
        try{
            OrderUtil orderUtil=new OrderUtil();
            com.igp.handles.admin.utils.Order.OrderUtil adminOrderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();
            List<String> orderProductsLists = Arrays.asList(orderProductIds.split(","));
            List<OrdersProducts> ordersProducts=orderUtil.getOrderProducts("1",orderId,fkAssociateId,ordersProductExtraInfoMap,orderProductIds,true);
            StringBuilder sb=new StringBuilder("VALUES");
            for (int i = 0; i < orderProductsLists.size(); i++) {
                if (i==orderProductsLists.size()-1){
                    sb.append("(" + orderProductsLists.get(i) + "," + orderId + "," + rejectionType + ",'" + vendorIssue + "',now())");
                }
                else {
                    sb.append("(" + orderProductsLists.get(i) + "," + orderId + "," + rejectionType + ",'" + vendorIssue + "',now()),");
                }
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO `handels_history` (`orders_products_id`,`orders_id`, `rejection_type` , `rejection_message`, `insertTime`) "+sb.toString()+" ON DUPLICATE KEY UPDATE orders_id="+orderId+",rejection_type="+rejectionType+",rejection_message='"+vendorIssue+"' ,insertTime= now();";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            int rows=preparedStatement.executeUpdate();
            if(rows>0){
                result=true;
                OrderStatusUpdateUtil.sendEmailToHandelsTeamToTakeAction(orderId,fkAssociateId,"Alternate Number/Address issues",vendorIssue);
                for(int i=0;i<ordersProducts.size();i++){
                    adminOrderUtil.insertIntoHandelOrderHistory(orderId, ordersProducts.get(i).getProductId(),Integer.parseInt(fkAssociateId),vendorIssue,ipAddress,userAgent,"instruction","to_igp");
                }
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }
    public List<VendorInstruction> getVendorInstructionNew(String fkAssociateId){
        List<VendorInstruction> vendorOrderInstruction=new ArrayList<>();
        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT orders_id,message from handel_order_history "
                + " where fk_associate_id = ? and insert_time >= date_add(now(),interval -2 day) and action = ? and sub_action = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,Integer.parseInt(fkAssociateId));
            preparedStatement.setString(2,"instruction");
            preparedStatement.setString(3,"from_igp");
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                VendorInstruction vendorInstruction=new VendorInstruction();
                vendorInstruction.setOrderId(resultSet.getString("orders_id"));
                vendorInstruction.setInstruction(resultSet.getString("message"));
                vendorOrderInstruction.add(vendorInstruction);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return vendorOrderInstruction;
    }
    public List<VendorInstruction> getVendorInstruction(String fkAssociateId){
        List<VendorInstruction> vendorOrderInstruction=new ArrayList<>();
        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT orders_id,instruction_msg from vendor_instructions "
                + " where associate_id = ? and insertTime >= date_add(now(),interval -2 day)";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,Integer.parseInt(fkAssociateId));
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                VendorInstruction vendorInstruction=new VendorInstruction();
                vendorInstruction.setOrderId(resultSet.getString("orders_id"));
                vendorInstruction.setInstruction(resultSet.getString("instruction_msg"));
                vendorOrderInstruction.add(vendorInstruction);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return vendorOrderInstruction;
    }
}
