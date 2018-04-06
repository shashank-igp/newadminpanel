package com.igp.handles.admin.utils.Order;

import com.igp.admin.mappers.marketPlace.Constants;
import com.igp.config.instance.Database;
import com.igp.handles.admin.mappers.Dashboard.DashboardMapper;
import com.igp.handles.admin.models.Order.OrderLogModel;
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
import java.util.*;

/**
 * Created by shanky on 22/1/18.
 */
public class OrderUtil {
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);


    public boolean insertIntoOrderHistory(int orderId,int vendorId,String ordersHistoryComment){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="insert into orders_history (fk_orders_id,fk_associate_login_id,fk_associate_user_id,orders_history_comment, "
                + " orders_history_time,fk_associate_id,fk_payout_reason_id,deliver_date) VALUES  ( ?,?,?,?,now(),?,0,'0000-00-00')";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId); // orderID
            preparedStatement.setString(2,"Handels"); //fk_associate_login_id
            preparedStatement.setString(3,"Handels"); //fk_associate_user_id
            preparedStatement.setString(4,ordersHistoryComment);
            preparedStatement.setInt(5,vendorId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();

            logger.debug("step-8 assignReassignOrder after insert into orders_history with insertion status = "+status);


            if (status == 0) {
                logger.error("Failed to insert Into OrderHistory ");
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

    public boolean updateOrderProduct(int orderProductId,int vendorId,Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            //            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE orders_products set orders_product_status = ? , fk_associate_id = ? , delivery_status = ? , delivery_attempt = ? where "
                + " orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,"Processed");
            preparedStatement.setInt(2,vendorId);
            preparedStatement.setInt(3,0);
            preparedStatement.setInt(4,0);
            preparedStatement.setInt(5,orderProductId);

            Integer status = preparedStatement.executeUpdate();

            logger.debug("step-5 assignReassignOrder after update in orders_products with update status = "+status);
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
    public boolean updateTrackorderAfterAssignReAssign(int vendorId,int orderProductId,Connection connection){
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            statement="UPDATE trackorders set fk_associate_id = ?, processedDate = now() , outForDeliveryDate = "
                + " '0000-00-00 00:00:00' , deliveredDate = '0000-00-00 00:00:00' where orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,vendorId); // orderID
            preparedStatement.setInt(2,orderProductId);

            Integer status = preparedStatement.executeUpdate();

            logger.debug("step-6 assignReassignOrder after update in trackorders with update status = "+status);

            if (status == 0) {
                logger.error("Failed to update trackorders ");
            } else {
                result=true;
            }


        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
        }
        return result;
    }
    public boolean deleteEntryFromVendorAssignPrice(int orderId,int productId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="DELETE from vendor_assign_price where orders_id = ? and products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId); // orderID
            preparedStatement.setInt(2,productId);

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
                    .datePurchased(resultSet.getString("date_purchased"))
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


    public int reassignOrderToVendor(int orderId,int orderproductId,int vendorId,Order order,String action,String ipAddress,String userAgent){
        int result=0;
        try{
            result=assignOrderToVendor(orderId,orderproductId,vendorId,order,action,ipAddress,userAgent);
        }catch (Exception exeception){

        }
        return result;
    }


    public int assignOrderToVendor(int orderId,int orderproductId,int vendorId,Order order,String action,String ipAddress,String userAgent){
        int result=0;
        VendorUtil vendorUtil=new VendorUtil();
        VendorAssignModel vendorAssignModel=new VendorAssignModel();
        List<OrderComponent> componentList=new ArrayList<>();
        double componentTotal=0.0,ordersubTotal=0.0,shippingCharge=0.0,vendorPrice=0.0;
        com.igp.handles.vendorpanel.utils.Order.OrderUtil orderUtil=new com.igp.handles.vendorpanel.utils.Order.OrderUtil();

        DashboardMapper dashboardMapper=new DashboardMapper();
        OrdersProducts ordersProducts=null;
        OrderProductExtraInfo orderProductExtraInfo=null;
        String ordersHistoryComment="",previousVendorName="";

        try {
            ordersProducts=order.getOrderProducts().get(0);
            orderProductExtraInfo=ordersProducts.getOrderProductExtraInfo();

            componentTotal=orderUtil.getProductComponents(ordersProducts.getProductId(),vendorId,
                ordersProducts.getProducts_code(),componentList,orderProductExtraInfo,false);
            vendorPrice=componentTotal*ordersProducts.getProductQuantity();
            if(orderProductExtraInfo.getDeliveryType()==4){
                shippingCharge=vendorUtil.getShippingChnargeForVendorOnPincode(vendorId,order.getDeliveryPostcode(),
                    1);
            }else {
                shippingCharge=vendorUtil.getShippingChnargeForVendorOnPincode(vendorId,order.getDeliveryPostcode(),
                    orderProductExtraInfo.getDeliveryType());
            }



            if(checkIfVendorHasAllProductComponent(vendorId,ordersProducts.getProducts_code())==false){
                return 2; // that means vendor could not process this order since vendor does not have all components needed
            }else if(checkIfCurrentVendorAlreadyAssignedToOrder(vendorId,orderId,ordersProducts.getProductId())){
                return 3; // cant allow to assign same order to same vendor again
            }

            logger.debug("step-3 assignReassignOrder ");


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
            vendorAssignModel.setShipppingType(Constants.getDeliveryType(orderProductExtraInfo.getDeliveryType()+""));
            vendorAssignModel.setDeliveryDate(orderProductExtraInfo.getDeliveryDate().toString());
            vendorAssignModel.setDeliveryTime(orderProductExtraInfo.getDeliveryTime());
            vendorAssignModel.setFlagEggless(0); //

            if(vendorUtil.insertIntoVendorAssignPrice(vendorAssignModel,orderproductId)){
                result= 1;
                previousVendorName=vendorUtil.getVendorInfo(Integer.parseInt(order.getOrderProducts().get(0).getFkAssociateId())).getAssociateName();
                ordersHistoryComment="The vendor of product "+order.getOrderProducts().get(0).getProductName()+" has been changed "
                    + "from "+previousVendorName+" To "
                    + vendorUtil.getVendorInfo(vendorId).getAssociateName()+" By New Handels Panel<Br>";

                logger.debug("step-7 assignReassignOrder after complete insertion in VAP,OP,trackorders");


                insertIntoOrderHistory(orderId,vendorId,ordersHistoryComment);
                createOrdersProductsComponentsInfo(componentList,order);

                if(action.equals("reassign")){
                    insertIntoHandelOrderHistory(orderId,ordersProducts.getProductId(),72,ordersHistoryComment,ipAddress,userAgent,"vendor_change","re_assign");
                }else {
                    insertIntoHandelOrderHistory(orderId,ordersProducts.getProductId(),72,ordersHistoryComment,ipAddress,userAgent,"vendor_change","assign");
                }
            }

        }catch (Exception exception){
            logger.error("Exception while assigning a order to vendor ", exception);
        }
        return result;
    }

    public boolean checkIfVendorHasAllProductComponent(int vendorId,String barcode){
        boolean result=true;
        Connection connection = null;
        ResultSet resultSet = null,resultSet1 = null;
        String statement,statement1,componentIds=null;
        PreparedStatement preparedStatement = null,preparedStatement1 = null;
        int numRows=0;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select concat('(',concat(group_concat(fk_component_id,''),')')) as componentIds "
                + " from AA_barcode_to_components where barcode = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,barcode);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                componentIds=resultSet.getString("componentIds");

                if( componentIds != null && !componentIds.equals("") ){

                    try{
                        statement1="select * from AA_vendor_to_components where fk_associate_id = ? and fk_component_id in "+componentIds ;
                        preparedStatement1 = connection.prepareStatement(statement1);
                        preparedStatement1.setInt(1,vendorId);

                        logger.debug("STATEMENT CHECK: " + preparedStatement1);
                        resultSet1 = preparedStatement1.executeQuery();

                        while (resultSet1.next()){
                            numRows=numRows+1;
                            String vendorComponentID=resultSet1.getString("fk_component_id");
                            if(!componentIds.contains(vendorComponentID)){
                                result=false;
                                break;
                            }
                        }
                        if(numRows==0){
                            result=false;
                        }
                    }catch (Exception exception){
                        logger.error("Exception in connection", exception);
                    }finally {
                        Database.INSTANCE.closeStatement(preparedStatement1);
                        Database.INSTANCE.closeResultSet(resultSet1);
                    }

                }else {
                    result=false;
                }

            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }
    public boolean updateShippingInVendorAssignPrice(int orderId,int productId,double shippingCharge){
        boolean result=true;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update vendor_assign_price set shipping = ? , order_subtotal = shipping + vendor_price where orders_id = ? and products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setDouble(1,shippingCharge);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setInt(3,productId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update shipping in vendor_assign_price ");
            } else {
                result=true;
            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }
    public boolean updateVendorAssignPrice(int orderId,int productId,
        Double vendorPrice,Double shippingCharge,int orderProductId,int vendorId,String ipAddress,String userAgent){
        boolean result=true;
        Connection connection = null;
        String statement,shippingChargeClause="",vendorPriceClause="";
        PreparedStatement preparedStatement = null;
        Order order=null;
        OrdersProducts ordersProducts=null;
        String orderHistoryComment=null,comment1="",comment2="";
        try{
            order=getOrderRelatedInfo(orderId,orderProductId);
            ordersProducts=order.getOrderProducts().get(0);
            if(shippingCharge!=null){
                shippingChargeClause+=" shipping = "+shippingCharge+" , ";
                comment1=" shipping price = "+shippingCharge;
            }
            if(vendorPrice!=null){
                vendorPriceClause+=" vendor_price =  "+vendorPrice+" , ";
                comment2=" , vendor Price = "+vendorPrice;
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update vendor_assign_price set "+vendorPriceClause+shippingChargeClause
                + "order_subtotal = shipping + vendor_price    where orders_id = ? and products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,productId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update vendor_price in vendor_assign_price ");
            } else {
                result=true;
                orderHistoryComment="Price changes happened for product "+ordersProducts.getProductName()+" to "+comment1+comment2;
                insertIntoOrderHistory(orderId,Integer.parseInt(ordersProducts.getFkAssociateId()),orderHistoryComment);
                insertIntoHandelOrderHistory(orderId,productId,72,orderHistoryComment,ipAddress,userAgent,"price_and_delivery_changes","price_change");
            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }

    public boolean updateComponentPriceOrderLevel(int orderId,int productId,int componentId,double componentPrice){
        boolean result=true;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update orders_products_components_info set vendor_to_component_price = ? where orders_id = ? "
                + " and products_id = ? and component_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setDouble(1,componentPrice);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setInt(3,productId);
            preparedStatement.setInt(4,componentId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update vendor_to_component_price in orders_products_components_info ");
            } else {
                result=true;
            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }

    public boolean createOrdersProductsComponentsInfo(List<OrderComponent> componentList,Order order){
        boolean result=false;
        try {
            for(int i=0;i<componentList.size();i++){
                insertIntoOrdersProductsComponentsInfo(componentList.get(i),order);
            }
        }catch (Exception exception){

        }
        return result;
    }
    public boolean insertIntoOrdersProductsComponentsInfo(OrderComponent orderComponent,Order order){
        boolean result=false;
        OrdersProducts ordersProducts=null;

        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try {
            ordersProducts=order.getOrderProducts().get(0);
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="insert into orders_products_components_info (orders_products_id,orders_id,products_id,products_code, "
                + " component_id,component_code,quantity,vendor_to_component_price) VALUES  ( ?,?,?,?,?,?,?,?) ON DUPLICATE "
                + " KEY update quantity= ? , vendor_to_component_price = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,ordersProducts.getOrderProductId()); // orderID
            preparedStatement.setInt(2,order.getOrderId()); //fk_associate_login_id
            preparedStatement.setInt(3,ordersProducts.getProductId()); //fk_associate_user_id
            preparedStatement.setString(4,ordersProducts.getProducts_code());
            preparedStatement.setInt(5,orderComponent.getComponentId());
            preparedStatement.setString(6,orderComponent.getComponentCode());
            preparedStatement.setDouble(7,Double.valueOf(orderComponent.getQuantity()));
            preparedStatement.setDouble(8,Double.valueOf(orderComponent.getComponentPrice()));
            preparedStatement.setDouble(9,Double.valueOf(orderComponent.getQuantity()));
            preparedStatement.setDouble(10,Double.valueOf(orderComponent.getComponentPrice()));
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to insert Into OrderHistory ");
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
    public boolean checkIfCurrentVendorAlreadyAssignedToOrder(int vendorId,int orderId,int productId){
        boolean result=false;
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from vendor_assign_price where orders_id = ? and products_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,productId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt("fk_associate_id")==vendorId){
                    result=true;
                }
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }

    public OrderComponent getOrderComponent(int orderId,int productId,int componentId){

        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        OrderComponent orderComponent=null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from orders_products_components_info where orders_id = ? and products_id = ? and component_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,productId);
            preparedStatement.setInt(3,componentId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                orderComponent=new OrderComponent.Builder()
                    .componentId(resultSet.getInt("component_id"))
                    .componentCode(resultSet.getString("component_code"))
                    .quantity(resultSet.getString("quantity"))
                    .componentPrice(resultSet.getString("vendor_to_component_price"))
                    .productId(resultSet.getString("products_id"))
                    .build();
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return orderComponent;
    }
    public List<OrdersProducts> getOrderProductList(String orderProductIds){
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        List<OrdersProducts> ordersProductsList=new ArrayList<>();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from orders_products as  op where op.orders_products_id in ( "+orderProductIds+" ) ";
            preparedStatement = connection.prepareStatement(statement);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
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
                    .build();
                ordersProductsList.add(ordersProducts);
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return ordersProductsList;
    }
    public boolean updateDeliveryDetails(int orderId,int orderProductId,int productId,String deliveryDate,String deliveryTime, int deliveryType,int vendorId,String ipAddress,String userAgent){
        boolean result=false;
        Connection connection = null;
        String statement,deliveryDateClause="",deliveryTimeClause="",deliveryTypeClause="";
        PreparedStatement preparedStatement = null;
        Integer status=null;
        Order order=null;
        OrdersProducts ordersProducts=null;
        OrderProductExtraInfo orderProductExtraInfo=null;
        String orderHistoryComment=null;
        try{
            order=getOrderRelatedInfo(orderId,orderProductId);
            ordersProducts=order.getOrderProducts().get(0);
            orderProductExtraInfo=ordersProducts.getOrderProductExtraInfo();
            if(deliveryType==3){
                deliveryTime="23:30 hrs - 23:59 hrs";
            }else if(deliveryType==1 || deliveryType == 4){
                deliveryTime="";
            }
            if(deliveryDate!=null){
                deliveryDateClause=" delivery_date = '"+deliveryDate+"' , ";
            }
            if(deliveryType!=0){
                deliveryTypeClause=" delivery_type = "+deliveryType+" , ";
            }
            if(deliveryTime != null){
                deliveryTimeClause=" delivery_time = '"+deliveryTime+"' , ";
            }


            connection = Database.INSTANCE.getReadWriteConnection();
            statement=" UPDATE order_product_extra_info set "+deliveryDateClause+deliveryTimeClause+deliveryTypeClause
                +" attributes = attributes where order_id = ? and order_product_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,orderProductId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            status = preparedStatement.executeUpdate();

            if (status == 0) {
                logger.error("Failed to update order_product_extra_info while updateDeliveryDetails ");
            } else {

                if(deliveryTime != null ){
                    if(deliveryType == 2){
                        deliveryTimeClause=" delivery_time = '"+deliveryTime.split(" - ")[0]+"' , ";
                    }else {
                        deliveryTimeClause=" delivery_time = '' ,";
                    }
                }
                if(deliveryType!=0){
                    deliveryTypeClause=" shipping_type = '"+Constants.getDeliveryType(deliveryType+"")+"' , ";
                }


                statement="UPDATE  vendor_assign_price set "+deliveryDateClause+deliveryTimeClause+deliveryTypeClause
                    +" assign_by_user = assign_by_user where orders_id = ? and  products_id = ? ";

                preparedStatement = connection.prepareStatement(statement);

                preparedStatement.setInt(1,orderId);
                preparedStatement.setInt(2,productId);
                logger.debug("STATEMENT CHECK: " + preparedStatement);
                status = preparedStatement.executeUpdate();

                if (status == 0) {
                    logger.error("Failed to update vendor_assign_price while updateDeliveryDetails ");
                } else{
                    if(deliveryDate!=null){
                        statement="UPDATE trackorders set date_of_delivery = ? where orders_id = ? and orders_products_id = ? ";
                        preparedStatement = connection.prepareStatement(statement);

                        preparedStatement.setString(1,deliveryDate);
                        preparedStatement.setInt(2,orderId);
                        preparedStatement.setInt(3,orderProductId);
                        logger.debug("STATEMENT CHECK: " + preparedStatement);
                        status = preparedStatement.executeUpdate();

                        if (status == 0) {
                            logger.error("Failed to update trackorders while updateDeliveryDetails ");
                        } else{
                            result=true;
                            orderHistoryComment="Delivery Detail changes for product "+ordersProducts.getProductName()+" from "
                                +orderProductExtraInfo.getDeliveryDate()+" "+orderProductExtraInfo.getDeliveryTime()+" "
                                +Constants.getDeliveryType(String.valueOf(orderProductExtraInfo.getDeliveryType()))+" to "
                                +deliveryDateClause+"  "+deliveryTimeClause+"  "+deliveryTypeClause;
                            insertIntoOrderHistory(orderId,Integer.parseInt(ordersProducts.getFkAssociateId()),orderHistoryComment);
                            insertIntoHandelOrderHistory(orderId,productId,72,orderHistoryComment,ipAddress,userAgent,"price_and_delivery_changes","delivery_changes");
                        }
                    }
                }
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }
    public List<OrderLogModel> getOrderLog(int orderId,String type){
        String insertTime="",log="";
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        List<OrderLogModel> orderLogModelList=new ArrayList<>();
        VendorUtil vendorUtil=new VendorUtil();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            if(type.equals("message")){
                statement="SELECT  * from handel_order_history where orders_id = ? and action = ?  order by handel_order_history_id desc";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,orderId);
                preparedStatement.setString(2,"instruction");
            }else{
                statement="SELECT  * from handel_order_history where orders_id = ? order by handel_order_history_id desc";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,orderId);
            }
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                OrderLogModel orderLogModel=new OrderLogModel();
                insertTime=resultSet.getString("insert_time");
                log=resultSet.getString("message").replaceAll("<Br>","");
                orderLogModel.setDate(insertTime.split(" ")[0]);
                orderLogModel.setTime(insertTime.split(" ")[1]);
                orderLogModel.setMessage(log);
                orderLogModel.setUser(vendorUtil.getVendorInfo(resultSet.getInt("fk_associate_id")).getAssociateName());
                if(resultSet.getString("action").equals("instruction")){
                    orderLogModel.setType("message");
                }else {
                    orderLogModel.setType("log");
                }

                orderLogModelList.add(orderLogModel);
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return orderLogModelList;
    }
    public boolean cancelOrder(int orderId,String orderProductIdString,String comment,String ipAddress,String userAgent){
        boolean result=false;
        Connection connection = null;
        String statement,orderHistoryComment="";
        PreparedStatement preparedStatement = null;
        try{
            List<OrdersProducts> ordersProductsList=getOrderProductList(orderProductIdString);
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update orders_products set orders_product_status = ?  where orders_products_id in ( "+ orderProductIdString +"  ) ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1,"Rejected");
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update orders_products while marking that orderProduct as cancelled ");
            } else {
                orderHistoryComment="order is cancelled , reason is "+comment;
                if(insertIntoOrderHistory(orderId,0,orderHistoryComment)){
                    result=true;
                }
                for(OrdersProducts ordersProducts:ordersProductsList){
                    insertIntoHandelOrderHistory(orderId,ordersProducts.getProductId(),72,orderHistoryComment,ipAddress,userAgent,"status_change","Rejected");
                }
            }

        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }
    public boolean updateOrderProductForApproveAttemptedDeliveryOrder(int orderId,String orderProductIdList,String ipAddress,String userAgent){
        boolean result=false;
        Connection connection = null;
        String statement,orderHistoryComment="";
        PreparedStatement preparedStatement = null;
        int vendorId=0;
        List<OrdersProducts> ordersProducts=null;
        try{
            ordersProducts=getOrderProductList(orderProductIdList);
            vendorId=Integer.parseInt(ordersProducts.get(0).getFkAssociateId());
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update orders_products set delivery_attempt = ?  where orders_products_id in ( "+orderProductIdList+" ) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,2);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update orders_products while approving that order for re-delivery attempt ");
            } else {
                orderHistoryComment="order is approve to be re-delivered by New Handels panel ";
                if(insertIntoOrderHistory(orderId,vendorId,orderHistoryComment)){
                    result=true;
                }
                for(OrdersProducts ordersProduct:ordersProducts){
                    insertIntoHandelOrderHistory(orderId,ordersProduct.getProductId(),72,orderHistoryComment,ipAddress,userAgent,"status_change","attempted");
                }
            }
        }catch(Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

        return result;
    }
    public boolean insertIntoHandelOrderHistory(int orderId,int productId,int fkAssociateId,String instruction,String ipAddress,String userAgent,String action,String subAction){
        boolean result=false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="INSERT INTO handel_order_history (orders_id,products_id,fk_associate_id,action,sub_action,message,ip,user_agent,insert_time) VALUES (?,?,?,?,?,?,?,?,now()) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,productId);
            preparedStatement.setInt(3,fkAssociateId);
            preparedStatement.setString(4,action);
            preparedStatement.setString(5,subAction);
            preparedStatement.setString(6,instruction);
            preparedStatement.setString(7,ipAddress);
            preparedStatement.setString(8,userAgent);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to insert handel_order_history");
            } else {
                result=true;
            }
        }catch(Exception exception){
            logger.error("Exception in connection", exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

}
