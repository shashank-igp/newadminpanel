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


    public boolean insertIntoOrderHistory(Order order,int vendorId,String ordersHistoryComment){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="insert into orders_history (fk_orders_id,fk_associate_login_id,fk_associate_user_id,orders_history_comment, "
                + " orders_history_time,fk_associate_id,fk_payout_reason_id,deliver_date) VALUES  ( ?,?,?,?,now(),?,0,'0000-00-00')";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,order.getOrderId()); // orderID
            preparedStatement.setString(2,"Handels"); //fk_associate_login_id
            preparedStatement.setString(3,"Handels"); //fk_associate_user_id
            preparedStatement.setString(4,ordersHistoryComment);
            preparedStatement.setInt(5,vendorId);
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

    public boolean updateOrderProduct(int orderProductId,int vendorId,Connection connection){
        //Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            //            connection = Database.INSTANCE.getReadWriteConnection();
            statement="UPDATE orders_products set orders_product_status = ? , fk_associate_id = ? , delivery_status = ? where "
                + " orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,"Processed");
            preparedStatement.setInt(2,vendorId);
            preparedStatement.setInt(3,0);
            preparedStatement.setInt(4,orderProductId);

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
    public boolean updateTrackorder(int vendorId,int orderProductId,Connection connection){
        PreparedStatement preparedStatement = null;
        String statement;
        boolean result=false;
        try{
            statement="UPDATE trackorders set fk_associate_id = ? where orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,vendorId); // orderID
            preparedStatement.setInt(2,orderProductId);

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


    public int reassignOrderToVendor(int orderId,int orderproductId,int vendorId){
        int result=0;
        try{
            result=assignOrderToVendor(orderId,orderproductId,vendorId);
        }catch (Exception exeception){

        }
        return result;
    }


    public int assignOrderToVendor(int orderId,int orderproductId,int vendorId){
        int result=0;
        Order order=null;
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
            order=getOrderRelatedInfo(orderId,orderproductId);
            ordersProducts=order.getOrderProducts().get(0);
            orderProductExtraInfo=ordersProducts.getOrderProductExtraInfo();

            componentTotal=orderUtil.getProductComponents(ordersProducts.getProductId(),vendorId,
                ordersProducts.getProducts_code(),componentList,orderProductExtraInfo);
            vendorPrice=componentTotal*ordersProducts.getProductQuantity();
            shippingCharge=vendorUtil.getShippingChnargeForVendorOnPincode(vendorId,order.getDeliveryPostcode(),
                orderProductExtraInfo.getDeliveryType());

            if(checkIfVendorHasAllProductComponent(vendorId,ordersProducts.getProducts_code())==false){
                return 2; // that means vendor could not process this order since vendor does not have all components needed
            }else if(checkIfCurrentVendorAlreadyAssignedToOrder(vendorId,orderId,ordersProducts.getProductId())){
                return 3; // cant allow to assign same order to same vendor again
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

            if(vendorUtil.insertIntoVendorAssignPrice(vendorAssignModel,orderproductId)){
                result= 1;
                previousVendorName=vendorUtil.getVendorInfo(Integer.parseInt(order.getOrderProducts().get(0).getFkAssociateId())).getAssociateName();
                ordersHistoryComment="The vendor of product "+order.getOrderProducts().get(0).getProductName()+" has been changed "
                    + "from "+previousVendorName+" To "
                    + vendorUtil.getVendorInfo(vendorId).getAssociateName()+" By New Handels Panel<Br>";
                insertIntoOrderHistory(order,vendorId,ordersHistoryComment);
                createOrdersProductsComponentsInfo(componentList,order);
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
        String statement,statement1,componentIds="";
        PreparedStatement preparedStatement = null,preparedStatement1 = null;
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

                if(!componentIds.equals("")){

                    try{
                        statement1="select * from AA_vendor_to_components where fk_associate_id = ? and fk_component_id in "+componentIds ;
                        preparedStatement1 = connection.prepareStatement(statement1);
                        preparedStatement1.setInt(1,vendorId);

                        logger.debug("STATEMENT CHECK: " + preparedStatement1);
                        resultSet1 = preparedStatement1.executeQuery();

                        while (resultSet1.next()){
                            String vendorComponentID=resultSet1.getString("fk_component_id");
                            if(!componentIds.contains(vendorComponentID)){
                                result=false;
                                break;
                            }
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
                                                            Double vendorPrice,Double shippingCharge){
        boolean result=true;
        Connection connection = null;
        String statement,shippingChargeClause="",vendorPriceClause="";
        PreparedStatement preparedStatement = null;
        try{
            if(shippingCharge!=null){
                shippingChargeClause+=" shipping = "+shippingCharge+" , ";
            }
            if(vendorPrice!=null){
                vendorPriceClause+=" vendor_price = vendor_price + "+vendorPrice+" , ";
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
    public int getProductId(int orderProductId){
        int productId=0;
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from orders_products where orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderProductId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                productId=resultSet.getInt("products_id");
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }

        return productId;
    }
    public boolean updateDeliveryDetails(int orderId,int orderProductId,int productId,String deliveryDate,String deliveryTime,
                                        int deliveryType){
        boolean result=false;
        Connection connection = null;
        String statement,deliveryDateClause="",deliveryTimeClause="",deliveryTypeClause="";
        PreparedStatement preparedStatement = null;
        DashboardMapper dashboardMapper=new DashboardMapper();
        Integer status=null;
        try{

            if(deliveryDate!=null){
                deliveryDateClause=" delivery_date = '"+deliveryDate+"' , ";
            }
            if(deliveryTime != null && deliveryType == 2){
                deliveryTimeClause=" delivery_time = '"+deliveryTime+"' , ";
            }
            if(deliveryType!=0){
                deliveryTypeClause=" delivery_type = "+deliveryType+" , ";
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

                if(deliveryTime != null && deliveryType == 2){
                    deliveryTimeClause=" delivery_time = '"+deliveryTime.split(" - ")[0]+"' , ";
                }
                if(deliveryType!=0){
                    deliveryTypeClause=" shipping_type = '"+dashboardMapper.getDeliveryType(deliveryType+"")+"' , ";
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
                        preparedStatement.setInt(3,productId);
                        logger.debug("STATEMENT CHECK: " + preparedStatement);
                        status = preparedStatement.executeUpdate();

                        if (status == 0) {
                            logger.error("Failed to update trackorders while updateDeliveryDetails ");
                        } else{
                            result=true;
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
    public String getOrderLog(int orderId){
        String logs="";
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from orders_history where fk_orders_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                logs+=resultSet.getString("orders_history_comment");
            }
        }catch (Exception exception){
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return logs;
    }
    public boolean cancelOrder(int orderId,int orderProductId,String comment){
        boolean result=false;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        Order order=null;
        try{
            order=getOrderRelatedInfo(orderId,orderProductId);
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update orders_products set orders_product_status = ?  where orders_products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1,"Rejected");
            preparedStatement.setInt(2,orderProductId);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to update orders_products while marking that orderProduct as cancelled ");
            } else {
                if(insertIntoOrderHistory(order,0,comment)){
                    result=true;
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

}
