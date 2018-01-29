package com.igp.handles.admin.utils.Order;

import com.igp.config.instance.Database;
import com.igp.handles.admin.mappers.Dashboard.DashboardMapper;
import com.igp.handles.admin.models.Vendor.VendorAssignModel;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Order.Order;
import com.igp.handles.vendorpanel.models.Order.OrderComponent;
import com.igp.handles.vendorpanel.models.Order.OrderProductExtraInfo;
import com.igp.handles.vendorpanel.models.Order.OrdersProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 22/1/18.
 */
public class OrderUtil {
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);
    
    public boolean updateOrderProduct(int orderProductId,int vendorId,Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
//            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE orders_products set orders_product_status = ? , fk_associate_id = ? , delivery_status = ? where "
                + " orders_prodcuts_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,"Processed"); // orderID
            preparedStatement.setInt(2,vendorId); // fk_associateId
            preparedStatement.setInt(3,orderProductId); //order_subtotal

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
            //Database.INSTANCE.closeConnection(connection);
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

    public Order getOrderRelatedInfo(int orderId,int orderProductId){
        Order order=null;
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        List<OrdersProducts> ordersProductsList=new ArrayList<>();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select * from orders o join orders_products op on o.orders_id = op.orders_id join order_product_extra_info "
                + " opei on op.orders_products_id = opei.order_product_id where o.orders_id = ? and op.orders_products_id = ? ";


            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,orderProductId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                OrderProductExtraInfo orderProductExtraInfo=new OrderProductExtraInfo.Builder()
                    .orderProductId(resultSet.getInt("opei.order_product_id"))
                    .orderId(resultSet.getInt("opei.order_id"))
                    .productId(resultSet.getInt("opei.product_id"))
                    .quantity(resultSet.getInt("opei.quantity"))
                    .attributes(resultSet.getString("opei.attributes"))
                    .giftBox(resultSet.getInt("opei.gift_box"))
                    .deliveryType(resultSet.getInt("opei.delivery_type"))
                    .deliveryDate(resultSet.getDate("opei.delivery_date"))
                    .deliveryTime(resultSet.getString("opei.delivery_time"))
                    .productCostPrice(resultSet.getInt("opei.product_cost_price"))
                    .build();

                OrdersProducts ordersProducts = new OrdersProducts.Builder()
                    .orderProductId(resultSet.getInt("op.orders_Products_Id"))
                    .orderId(resultSet.getInt("op.orders_id"))
                    .productId(resultSet.getInt("op.products_id"))
                    .productName(resultSet.getString("op.products_name"))
                    .productQuantity(resultSet.getInt("op.products_quantity"))
                    .products_code(resultSet.getString("op.products_code"))
                    .fkAssociateId(resultSet.getString("op.fk_associate_id"))
                    .ordersProductStatus(resultSet.getString("op.orders_product_status"))
                    .deliveryStatus(resultSet.getInt("op.delivery_status"))
                   .orderProductExtraInfo(orderProductExtraInfo)
                    .build();
                ordersProductsList.add(ordersProducts);

                order=new Order.Builder()
                    .orderId(resultSet.getInt("orders_id"))
                    .customerId(resultSet.getLong("customers_id"))
                    .customersCity(resultSet.getString("customers_city"))
                    .customersPostcode(resultSet.getString("customers_postcode"))
                    .customersCountry(resultSet.getString("customers_country"))
                    .deliveryCity(resultSet.getString("delivery_city"))
                    .deliveryPostcode(resultSet.getString("delivery_postcode"))
                    .deliveryState(resultSet.getString("delivery_state"))
                    .deliveryCountry(resultSet.getString("delivery_country"))
                    .deliveryEmail(resultSet.getString("delivery_email_address"))
                    .deliveryMobile(resultSet.getString("delivery_mobile"))
                    .datePurchased(resultSet.getDate("date_purchased"))
                    .orderProducts(ordersProductsList)
                    .build();
            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return order;
    }
    public boolean assignOrderToVendor(int orderId,int orderproductId,int vendorId){
        boolean result=false;
        Order order=null;
        VendorUtil vendorUtil=new VendorUtil();
        VendorAssignModel vendorAssignModel=new VendorAssignModel();
        List<OrderComponent> componentListForNewVendor=new ArrayList<>();
        List<OrderComponent> componentListForOldVendor=new ArrayList<>();
        double componentTotal=0.0,ordersubTotal=0.0,shippingCharge=0.0,vendorPrice=0.0;
        com.igp.handles.vendorpanel.utils.Order.OrderUtil orderUtil=new com.igp.handles.vendorpanel.utils.Order.OrderUtil();

        DashboardMapper dashboardMapper=new DashboardMapper();
        OrdersProducts ordersProducts=null;
        OrderProductExtraInfo orderProductExtraInfo=null;

        try {
            order=getOrderRelatedInfo(orderId,orderproductId);
            ordersProducts=order.getOrderProducts().get(0);
            orderProductExtraInfo=ordersProducts.getOrderProductExtraInfo();

            orderUtil.getProductComponents(ordersProducts.getProductId(),Integer.parseInt(ordersProducts.getFkAssociateId()),
                                ordersProducts.getProducts_code(),componentListForOldVendor, orderProductExtraInfo);

            componentTotal=orderUtil.getProductComponents(ordersProducts.getProductId(),vendorId,
                ordersProducts.getProducts_code(),componentListForNewVendor,orderProductExtraInfo);
            vendorPrice=componentTotal*ordersProducts.getProductQuantity();
            shippingCharge=vendorUtil.getShippingChnargeForVendorOnPincode(vendorId,order.getDeliveryPostcode(),
                                                            orderProductExtraInfo.getDeliveryType());

            if(checkComponentListSimilarity(componentListForNewVendor,componentListForOldVendor)==false){
                return false;
            }


            ordersubTotal=vendorPrice+shippingCharge;

            vendorAssignModel.setOrderId(orderId);
            vendorAssignModel.setFkAssociateId(vendorId);
            vendorAssignModel.setOrderSubtotal(ordersubTotal);
            vendorAssignModel.setShipping(shippingCharge);
            vendorAssignModel.setVendorPrice(vendorPrice);
            vendorAssignModel.setProductId(ordersProducts.getProductId());
            vendorAssignModel.setProductName(ordersProducts.getProductName());
            vendorAssignModel.setProductCode(ordersProducts.getProducts_code());
            vendorAssignModel.setCity(order.getDeliveryCity());
            vendorAssignModel.setMailRemainderFlag(0);
            vendorAssignModel.setAssignThrough(3);
            vendorAssignModel.setAssignByUser("NewHandelsPanel");
            vendorAssignModel.setShipppingType(dashboardMapper.getDeliveryType(orderProductExtraInfo.getDeliveryType()+""));
            vendorAssignModel.setDeliveryDate(orderProductExtraInfo.getDeliveryDate().toString());
            vendorAssignModel.setDeliveryTime(orderProductExtraInfo.getDeliveryTime());
            vendorAssignModel.setFlagEggless(0); //

            result=vendorUtil.insertIntoVendorAssignPrice(vendorAssignModel,orderproductId);

        }catch (Exception exception){
            logger.error("Exception while assigning a order to vendor ", exception);
        }
        return result;
    }

    public boolean checkComponentListSimilarity(List<OrderComponent> componentListForNewVendor,List<OrderComponent> componentListForOldVendor){
        boolean result=true;

        if(componentListForNewVendor.size() != componentListForOldVendor.size()){
            result=false;
        }else{
            for(int i=0;i<componentListForNewVendor.size();i++){

                int newComponentId=componentListForNewVendor.get(i).getComponentId();
                boolean flag=false;
                for(int j=0;j<componentListForOldVendor.size();j++){
                        if(newComponentId==componentListForOldVendor.get(j).getComponentId()){
                            flag=true;
                            break;
                        }
                }
                if(flag==false){
                    result=false;
                    break;
                }

            }
        }
        return result;
    }

}
