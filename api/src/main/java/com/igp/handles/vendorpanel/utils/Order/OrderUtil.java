package com.igp.handles.vendorpanel.utils.Order;

import com.igp.config.instance.Database;
import com.igp.handles.admin.mappers.Order.OrderMapper;
import com.igp.handles.admin.utils.Order.SlaCompliant;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import com.igp.handles.vendorpanel.models.Order.Order;
import com.igp.handles.vendorpanel.models.Order.OrderComponent;
import com.igp.handles.vendorpanel.models.Order.OrderProductExtraInfo;
import com.igp.handles.vendorpanel.models.Order.OrdersProducts;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanky on 10/7/17.
 */
public class OrderUtil
{
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);

    public List<OrdersProducts> getOrderProducts(String scopeId, int orderId, String fkAssociateId,Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap
        , String orderProductIds,boolean forAdminPanelOrNot){
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        VendorUtil vendorUtil=new VendorUtil();
        List<OrdersProducts> listOfOrderProducts=new ArrayList<>();
        String vendorIdClaus="";
        OrderMapper orderMapper=new OrderMapper();
        SlaCompliant slaCompliant=new SlaCompliant();
        try{

            if(forAdminPanelOrNot == false){
                vendorIdClaus=" op.fk_associate_id = "+fkAssociateId+" AND ";
            }

            connection = Database.INSTANCE.getReadOnlyConnection();
            if(orderProductIds.equalsIgnoreCase("0")){
                statement = "select op.*,opei.*,npei.m_img , p.update_date_time, p.products_name_for_url, "
                    + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN vap.assign_time END ) "
                    + " as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from"
                    + " orders_products as op left join vendor_assign_price vap on op.orders_id = vap.orders_id and "
                    + " vap.products_id = op.products_id inner join trackorders as track on op.orders_id = track.orders_id "
                    + " and op.products_id = track.products_id join  order_product_extra_info as opei on op.orders_products_id "
                    + " = opei.order_product_id  join products as p on op.products_id=p.products_id join "
                    + "newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on "
                    + " op.fk_associate_id=a.associate_id where "+vendorIdClaus+" op.orders_id=? AND p.fk_associate_id = 72 ";

            }else {
                statement = "select op.*,opei.*,npei.m_img , p.update_date_time, p.products_name_for_url, "
                    + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN vap.assign_time END ) "
                    + " as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from"
                    + " orders_products as op left join vendor_assign_price vap on op.orders_id = vap.orders_id and "
                    + " vap.products_id = op.products_id inner join trackorders as track on op.orders_id = track.orders_id "
                    + " and op.products_id = track.products_id join  order_product_extra_info as opei on op.orders_products_id "
                    + " = opei.order_product_id  join products as p on op.products_id=p.products_id join "
                    + "newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on "
                    + " op.fk_associate_id=a.associate_id where  op.orders_id=? AND p.fk_associate_id = 72 AND "
                    + vendorIdClaus + " op.orders_products_id IN ( "+orderProductIds+" )";

            }
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int slaCode=-99;
                OrdersProducts ordersProducts = new OrdersProducts.Builder()
                    .orderProductId(resultSet.getInt("op.orders_Products_Id"))
                    .orderId(resultSet.getInt("op.orders_id"))
                    .productId(resultSet.getInt("op.products_id"))
                    .productName(resultSet.getString("op.products_name"))
                    .productPrice(resultSet.getBigDecimal("op.products_price"))
                    .productPrice_inr(resultSet.getFloat("op.products_price_inr"))
                    .productQuantity(resultSet.getInt("op.products_quantity"))
                    .productSize(resultSet.getString("op.products_size"))
                    .products_weight(resultSet.getString("op.products_weight"))
                    .ordersProductStatus(resultSet.getString("op.orders_product_status"))
                    .products_code(resultSet.getString("op.products_code"))
                    .fkAssociateId(resultSet.getString("op.fk_associate_id"))
                    .deliveryStatus(resultSet.getInt("op.delivery_status"))
                    .productImage(resultSet.getString("npei.m_img"))
                    .productUpdateDateTime(dateFormat.parse(resultSet.getString("p.update_date_time")))
                    .productNameForUrl(resultSet.getString("p.products_name_for_url"))
                    .slaCode(resultSet.getInt("op.sla_code"))
                    .personalized(resultSet.getBoolean("npei.flag_personalize"))
                    .timeSlaVoilates(getTimeWhenColorChangesforOrderProduct(resultSet.getInt("op.sla_code")))
                    .deliveryAttemptFlag(resultSet.getInt("op.delivery_attempt"))
                    .build();

//                ordersProducts.setVendorName(vendorUtil.getVendorInfo(Integer.parseInt(ordersProducts.getFkAssociateId())).getAssociateName());
                ordersProducts.setVendorName(resultSet.getString("vendorName"));
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

                OrderDetailsPerOrderProduct orderDetailsPerOrderProduct = new OrderDetailsPerOrderProduct();
                orderDetailsPerOrderProduct.setAssignTime(resultSet.getString("assignTime"));
                orderDetailsPerOrderProduct.setPurchasedTime(resultSet.getString("datePurchased"));
                orderDetailsPerOrderProduct.setDeliveryDate(orderProductExtraInfo.getDeliveryDate());
                orderDetailsPerOrderProduct.setShippingType(String.valueOf(orderProductExtraInfo.getDeliveryType()));
                orderDetailsPerOrderProduct.setDeliveryStatus(ordersProducts.getDeliveryStatus()==1 ? true:false);
                orderDetailsPerOrderProduct.setOrderProductStatus(ordersProducts.getOrdersProductStatus());
                orderDetailsPerOrderProduct.setDeliveryTime(orderProductExtraInfo.getDeliveryTime());
                orderDetailsPerOrderProduct.setOrdersId(new Long(ordersProducts.getOrderId()));
                orderDetailsPerOrderProduct.setOrdersProductsId(new Long(ordersProducts.getOrderProductId()));
                orderDetailsPerOrderProduct.setVendorId(Integer.parseInt(ordersProducts.getFkAssociateId()));
                slaCode = slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,forAdminPanelOrNot==true ? 1 : 0);
                if(slaCode>0){
                    ordersProducts.setSlaCode(slaCode);
                    ordersProducts.setSlaFlag(OrderUtil.isSLASatisfied(slaCode));
                    ordersProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(slaCode));
                    if(forAdminPanelOrNot){
                        String[] catSubCatArray=orderMapper.findCategoryAndSubCategory(ordersProducts.getOrdersProductStatus(),Integer.parseInt(ordersProducts.getFkAssociateId()),ordersProducts.getDeliveryStatus(),slaCode);
                        ordersProducts.setOrderProductCategory(catSubCatArray[0]);
                        ordersProducts.setGetOrderProductSubCategory(catSubCatArray[1]);
                    }
                }else{// this is just a fallback if on the fly sla didnt work then it will take from db
                    ordersProducts.setSlaFlag(OrderUtil.isSLASatisfied(ordersProducts.getSlaCode()));
                    ordersProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(ordersProducts.getSlaCode()));
                    if(forAdminPanelOrNot){
                        String[] catSubCatArray=orderMapper.findCategoryAndSubCategory(ordersProducts.getOrdersProductStatus(),Integer.parseInt(ordersProducts.getFkAssociateId()),ordersProducts.getDeliveryStatus(),ordersProducts.getSlaCode());
                        ordersProducts.setOrderProductCategory(catSubCatArray[0]);
                        ordersProducts.setGetOrderProductSubCategory(catSubCatArray[1]);
                    }
                }

                if(ordersProducts.getOrdersProductStatus().equals("Shipped")){
                    if(ordersProducts.getDeliveryStatus()==0){
                        ordersProducts.setOrdersProductStatus("OutForDelivery");
                    }else {
                        if(ordersProducts.getDeliveryAttemptFlag()==1){
                            ordersProducts.setOrdersProductStatus("AttemptedDelivery");
                        }else if(ordersProducts.getDeliveryAttemptFlag()==2) {
                            ordersProducts.setOrdersProductStatus("ApprovedAttemptedDelivery");
                        } else {
                            ordersProducts.setOrdersProductStatus("Shipped");
                        }
                    }
                }

                if(forAdminPanelOrNot==false){
                    if(orderProductExtraInfo.getDeliveryType()==2){
                        String[] timeSlotArray=orderProductExtraInfo.getDeliveryTime().split(" hrs - ");
                        int secondTimeSlot=Integer.parseInt(timeSlotArray[1].substring(0,2))-1;
                        orderProductExtraInfo.setDeliveryTime(timeSlotArray[0]+" hrs - "+
                            secondTimeSlot+timeSlotArray[1].substring(2,timeSlotArray[1].length()));
                    }
                }
                ordersProductExtraInfoMap.put(Integer.valueOf(ordersProducts.getOrderProductId()),orderProductExtraInfo);
                listOfOrderProducts.add(ordersProducts);
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return listOfOrderProducts;
    }

    public List<OrdersProducts> getOrderProductsByStatusDate(String scopeId, int fkAssociateId, String status,
        Date date, String section, boolean isfuture,Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap,
        boolean forAdminPanelOrNot,String slaClause,int deliveryAttemptFlag){

        //  forAdminPanelOrNot  == true i.e request comes from admin panel and if == false then request is for vendor Panel
        Connection connection = null;
        ResultSet resultSet = null;
        String statement="",fkAssociateIdWhereClause="",deliveryAttemptClause;
        PreparedStatement preparedStatement = null;
        VendorUtil vendorUtil=new VendorUtil();
        List<OrdersProducts> listOfOrderProducts=new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SlaCompliant slaCompliant=new SlaCompliant();
        OrderMapper orderMapper=new OrderMapper();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            String operator = isfuture ? " >= " : " = ";
            if(forAdminPanelOrNot==true ){
                if(fkAssociateId==72){
                    fkAssociateIdWhereClause="op.fk_associate_id = 72  and  ";
                }else {
                    fkAssociateIdWhereClause="op.fk_associate_id != 72 and p.fk_associate_id = 72 and ";
                }
            }else {
                fkAssociateIdWhereClause="op.fk_associate_id = "+fkAssociateId+" and ";
            }
            if(slaClause!=null){
                slaClause="and op.sla_code in "+slaClause;
            }else {
                slaClause="";
            }
            deliveryAttemptClause=" and op.delivery_attempt = "+deliveryAttemptFlag+" ";
            switch (status)
            {
                case "Processing":
                    if (section.equals("past"))
                    {
                        Date todayDate = new Date();
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from orders_products op left join vendor_assign_price vap on "
                            + " op.orders_id = vap.orders_id and vap.products_id = op.products_id inner join trackorders "
                            + " as track on op.orders_id = track.orders_id and op.products_id = track.products_id    join order_product_extra_info "
                            + " as opei on op.orders_products_id=opei.order_product_id join products as p on "
                            + " op.products_id=p.products_id join newigp_product_extra_info as  npei on npei.products_id=p.products_id "
                            + " LEFT JOIN associate as a on op.fk_associate_id=a.associate_id where opei.product_id = op.products_id and p.fk_associate_id = 72  and " + fkAssociateIdWhereClause +  "  opei.delivery_date "
                            + " >= ? and  opei.delivery_date < '" + new SimpleDateFormat("yyyy-MM-dd").format(todayDate)
                            + "' and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc ";
                    }
                    else
                    {
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize ,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from orders_products "
                            + " op left join vendor_assign_price vap on op.orders_id = vap.orders_id and vap.products_id "
                            + " = op.products_id inner join trackorders as track on op.orders_id = track.orders_id "
                            + " and op.products_id = track.products_id join order_product_extra_info as opei on op.orders_products_id "
                            + " =opei.order_product_id join products as p on op.products_id=p.products_id join "
                            + "newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where opei.product_id = op.products_id and p.fk_associate_id = 72 and " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + operator + " ? and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc";

                    }
                    break;
                case "Processed":
                    if (section.equals("past"))
                    {
                        Date todayDate = new Date();
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products "
                            + " op left join vendor_assign_price vap on op.orders_id = vap.orders_id and vap.products_id "
                            + " = op.products_id inner join trackorders as track on op.orders_id = track.orders_id "
                            + " and op.products_id = track.products_id join order_product_extra_info as opei on op.orders_products_id "
                            + " =opei.order_product_id join products as p on op.products_id=p.products_id join "
                            + "newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where  " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + " >= ? and p.fk_associate_id = 72 and  opei.delivery_date < '" + new SimpleDateFormat("yyyy-MM-dd").format(todayDate)
                            + "' and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc ";
                    }
                    else
                    {
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products "
                            + " op left join vendor_assign_price vap on op.orders_id = vap.orders_id and vap.products_id "
                            + " = op.products_id inner join trackorders as track on op.orders_id = track.orders_id "
                            + " and op.products_id = track.products_id join order_product_extra_info as opei on op.orders_products_id "
                            + " = opei.order_product_id join products as p on op.products_id=p.products_id join "
                            + "newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + operator + " ? and p.fk_associate_id = 72 and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc";

                    }
                    break;
                case "Confirmed":
                    Date todayDate = new Date();
                    if (section.equals("past"))
                    {
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products "
                            + " op inner join vendor_assign_price vap on op.orders_id = vap.orders_id inner join trackorders "
                            + " as track on  op.orders_id = track.orders_id and op.products_id = track.products_id    join order_product_extra_info "
                            + " as opei on op.orders_products_id=opei.order_product_id join products as p on "
                            + " op.products_id=p.products_id join newigp_product_extra_info as  npei on npei.products_id=p.products_id "
                            + " LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where vap.products_id = op.products_id and " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + " >= ? and  opei.delivery_date < '" + new SimpleDateFormat("yyyy-MM-dd").format(todayDate)
                            + "' and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc ";
                    }else if(section.equals("tillToday")){
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from orders_products "
                            + " op inner join vendor_assign_price vap on op.orders_id = vap.orders_id inner join trackorders "
                            + " as track on op.orders_id = track.orders_id and op.products_id = track.products_id    join order_product_extra_info as "
                            + " opei on op.orders_products_id=opei.order_product_id join products as p on op.products_id "
                            + " = p.products_id join newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT "
                            + " JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where vap.products_id = op.products_id and " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + " >= ? and  opei.delivery_date <= '" + new SimpleDateFormat("yyyy-MM-dd").format(todayDate)
                            + "' and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc ";
                    }
                    else
                    {
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products "
                            + " op inner join vendor_assign_price vap on op.orders_id = vap.orders_id inner join trackorders "
                            + " as track on op.orders_id = track.orders_id and op.products_id = track.products_id    join order_product_extra_info as "
                            + " opei on op.orders_products_id=opei.order_product_id join products as p on op.products_id "
                            + " = p.products_id join newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where vap.products_id = op.products_id and " + fkAssociateIdWhereClause +  " opei.delivery_date "
                            + operator + " ? and op.orders_product_status= '" + status + "' "+ slaClause +" order by opei.delivery_date asc";

                    }
                    break;
                case "OutForDelivery":
                {
                    Date pastDate = DateUtils.addDays(date, -7); // get past 7
                    statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                        + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                        + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products as "
                        + " op inner join vendor_assign_price vap on op.orders_id = vap.orders_id inner join trackorders as "
                        + " track on op.orders_id = track.orders_id join order_product_extra_info as opei on "
                        + " op.orders_products_id=opei.order_product_id join products as p on op.products_id=p.products_id "
                        + " join newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                        + " where vap.products_id = op.products_id and op.products_id = track.products_id and " + fkAssociateIdWhereClause +  " op.delivery_status = 0 and  "
                        + " DATE_FORMAT(track.outForDeliveryDate,'%Y-%m-%d') <= ? and DATE_FORMAT(track.outForDeliveryDate,'%Y-%m-%d') >='"
                        + new SimpleDateFormat("yyyy-MM-dd").format(pastDate)
                        + "' and DATE_FORMAT(track.date_of_delivery,'%Y-%m-%d') >='"
                        +new SimpleDateFormat("yyyy-MM-dd").format(pastDate)
                        + "' and op.orders_product_status='Shipped' "+slaClause+" order by track.deliveryDate asc ";

                }
                break;
                case "Shipped":
                    if(deliveryAttemptFlag==1){
                        Date pastDate = DateUtils.addDays(date, -7); // get past 7
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from orders_products "
                            + " as op inner join vendor_assign_price vap on op.orders_id = vap.orders_id inner join trackorders "
                            + " as track on op.orders_id = track.orders_id join order_product_extra_info as opei on "
                            + " op.orders_products_id=opei.order_product_id join products as p on op.products_id "
                            + " = p.products_id join newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where vap.products_id = op.products_id and op.products_id = track.products_id and " + fkAssociateIdWhereClause +  " op.delivery_status = 1 and  "
                            +  " opei.delivery_date >= '"+ new SimpleDateFormat("yyyy-MM-dd").format(pastDate) + "' and  opei.delivery_date <= ? "
                            + " and op.orders_product_status='Shipped' "+ deliveryAttemptClause +" order by track.deliveryDate asc ";
                    }else {
                        statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                            + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                            + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName from orders_products "
                            + " as op inner join vendor_assign_price vap on op.orders_id = vap.orders_id "
                            + " inner join trackorders as track on op.orders_id = track.orders_id join "
                            + " order_product_extra_info as opei on op.orders_products_id=opei.order_product_id join "
                            + " products as p on op.products_id=p.products_id join "
                            + " newigp_product_extra_info as  npei on npei.products_id=p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id "
                            + " where vap.products_id = op.products_id and op.products_id = track.products_id and " + fkAssociateIdWhereClause +  " op.delivery_status = 1 and  "
                            + " DATE_FORMAT(track.deliveredDate,'%Y-%m-%d') = ? and op.orders_product_status='Shipped' "+ deliveryAttemptClause +" order by track.deliveryDate asc ";
                    }
                    break;
                case "all":
                    statement = "select op.*,opei.*,npei.m_img, p.update_date_time, p.products_name_for_url, "
                        + " npei.flag_personalize,( CASE WHEN vap.assign_time !='0000-00-00 00:00:00' THEN "
                        + " vap.assign_time END ) as assignTime,track.date_purchased datePurchased,a.associate_name as vendorName  from orders_products "
                        + " op inner join vendor_assign_price vap on op.orders_id = vap.orders_id "
                        + " inner join trackorders as track on op.orders_id = track.orders_id and op.products_id = track.products_id    join "
                        + " order_product_extra_info as opei on op.orders_products_id=opei.order_product_id join products as p "
                        + " on op.products_id=p.products_id join newigp_product_extra_info as  npei on npei.products_id "
                        + " = p.products_id LEFT JOIN associate as a on op.fk_associate_id=a.associate_id where vap.products_id "
                        + " = op.products_id and " + fkAssociateIdWhereClause +  " opei.delivery_date "
                        + operator + " ? and op.orders_product_status in ('Processed','Confirmed','Shipped') order by opei.delivery_date asc";
                    break;
                default:
                    break;
            }
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,new SimpleDateFormat("yyyy-MM-dd").format(date));
            logger.debug("sql query "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int slaCode=-99;
                OrdersProducts ordersProducts = new OrdersProducts.Builder()
                    .orderProductId(resultSet.getInt("op.orders_Products_Id"))
                    .orderId(resultSet.getInt("op.orders_id"))
                    .productId(resultSet.getInt("op.products_id"))
                    .productName(resultSet.getString("op.products_name"))
                    .productPrice(resultSet.getBigDecimal("op.products_price"))
                    .productPrice_inr(resultSet.getFloat("op.products_price_inr"))
                    .productQuantity(resultSet.getInt("op.products_quantity"))
                    .products_code(resultSet.getString("op.products_code"))
                    .fkAssociateId(resultSet.getString("op.fk_associate_id"))
                    .ordersProductStatus(resultSet.getString("op.orders_product_status"))
                    .SpecialChargesShip(resultSet.getLong("op.Special_charges_ship"))
                    .shippingTypeG(resultSet.getString("op.shipping_type_g"))
                    .deliveryStatus(resultSet.getInt("op.delivery_status"))
                    .productImage(resultSet.getString("npei.m_img"))
                    .productUpdateDateTime(dateFormat.parse(resultSet.getString("p.update_date_time")))
                    .productNameForUrl(resultSet.getString("p.products_name_for_url"))
                    .slaCode(resultSet.getInt("op.sla_code"))
                    .personalized(resultSet.getBoolean("npei.flag_personalize"))
                    .timeSlaVoilates(getTimeWhenColorChangesforOrderProduct(resultSet.getInt("op.sla_code")))
                    .deliveryAttemptFlag(resultSet.getInt("op.delivery_attempt"))
                    .build();

//                ordersProducts.setVendorName(vendorUtil.getVendorInfo(Integer.parseInt(ordersProducts.getFkAssociateId())).getAssociateName());
                ordersProducts.setVendorName(resultSet.getString("vendorName"));
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

                OrderDetailsPerOrderProduct orderDetailsPerOrderProduct = new OrderDetailsPerOrderProduct();
                orderDetailsPerOrderProduct.setAssignTime(resultSet.getString("assignTime"));
                orderDetailsPerOrderProduct.setPurchasedTime(resultSet.getString("datePurchased"));
                orderDetailsPerOrderProduct.setDeliveryDate(orderProductExtraInfo.getDeliveryDate());
                orderDetailsPerOrderProduct.setShippingType(String.valueOf(orderProductExtraInfo.getDeliveryType()));
                orderDetailsPerOrderProduct.setDeliveryStatus(ordersProducts.getDeliveryStatus()==1 ? true:false);
                orderDetailsPerOrderProduct.setOrderProductStatus(ordersProducts.getOrdersProductStatus());
                orderDetailsPerOrderProduct.setDeliveryTime(orderProductExtraInfo.getDeliveryTime());
                orderDetailsPerOrderProduct.setOrdersId(new Long(ordersProducts.getOrderId()));
                orderDetailsPerOrderProduct.setOrdersProductsId(new Long(ordersProducts.getOrderProductId()));
                orderDetailsPerOrderProduct.setVendorId(Integer.parseInt(ordersProducts.getFkAssociateId()));
                slaCode = slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,forAdminPanelOrNot==true ? 1 : 0);
                if(slaCode>0){
                    ordersProducts.setSlaCode(slaCode);
                    ordersProducts.setSlaFlag(OrderUtil.isSLASatisfied(slaCode));
                    ordersProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(slaCode));
                    if(forAdminPanelOrNot){
                        String[] catSubCatArray=orderMapper.findCategoryAndSubCategory(ordersProducts.getOrdersProductStatus(),Integer.parseInt(ordersProducts.getFkAssociateId()),ordersProducts.getDeliveryStatus(),slaCode);
                        ordersProducts.setOrderProductCategory(catSubCatArray[0]);
                        ordersProducts.setGetOrderProductSubCategory(catSubCatArray[1]);
                    }
                }else{// this is just a fallback if on the fly sla didnt work then it will take from db
                    ordersProducts.setSlaFlag(OrderUtil.isSLASatisfied(ordersProducts.getSlaCode()));
                    ordersProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(ordersProducts.getSlaCode()));
                    if(forAdminPanelOrNot){
                        String[] catSubCatArray=orderMapper.findCategoryAndSubCategory(ordersProducts.getOrdersProductStatus(),Integer.parseInt(ordersProducts.getFkAssociateId()),ordersProducts.getDeliveryStatus(),ordersProducts.getSlaCode());
                        ordersProducts.setOrderProductCategory(catSubCatArray[0]);
                        ordersProducts.setGetOrderProductSubCategory(catSubCatArray[1]);
                    }
                }

                if(ordersProducts.getOrdersProductStatus().equals("Shipped")){
                    if(ordersProducts.getDeliveryStatus()==0){
                        ordersProducts.setOrdersProductStatus("OutForDelivery");
                    }else {
                        if(ordersProducts.getDeliveryAttemptFlag()==1){
                            ordersProducts.setOrdersProductStatus("AttemptedDelivery");
                        }else if(ordersProducts.getDeliveryAttemptFlag()==2) {
                            ordersProducts.setOrdersProductStatus("ApprovedAttemptedDelivery");
                        } else {
                            ordersProducts.setOrdersProductStatus("Shipped");
                        }
                    }
                }

                if(forAdminPanelOrNot==false){
                    if(orderProductExtraInfo.getDeliveryType()==2){
                        String[] timeSlotArray=orderProductExtraInfo.getDeliveryTime().split(" hrs - ");
                        int secondTimeSlot=Integer.parseInt(timeSlotArray[1].substring(0,2))-1;
                        orderProductExtraInfo.setDeliveryTime(timeSlotArray[0]+" hrs - "+
                            secondTimeSlot+timeSlotArray[1].substring(2,timeSlotArray[1].length()));
                    }
                }
                ordersProductExtraInfoMap.put(Integer.valueOf(ordersProducts.getOrderProductId()),orderProductExtraInfo);
                listOfOrderProducts.add(ordersProducts);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return listOfOrderProducts;
    }
    public String[] getOrderProductVendorPrice(int fkAssociateId, int orderId, String productId){
        String[] resultArray=new String[4];
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select shipping, vendor_price , delivery_date , assign_time from vendor_assign_price where "
                + " fk_associate_id = ? and orders_id = ? and products_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);
            preparedStatement.setInt(2, orderId);
            preparedStatement.setString(3,productId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            resultArray[0]="0.00";
            resultArray[1]="0.00";
            resultArray[2]="";
            resultArray[3]=null;
            if(resultSet.next()){
                resultArray[0]=resultSet.getString("shipping");
                resultArray[1]=resultSet.getString("vendor_price");
                resultArray[2]=resultSet.getString("delivery_date");
                resultArray[3]=resultSet.getString("assign_time");
            }

        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }


        return resultArray;

    }

    public double getProductComponents(int productId,int fkAssociateId,String productCode,
        List<OrderComponent> componentList ,OrderProductExtraInfo orderProductExtraInfo
        ,boolean clubEgglessComponentFalg){

        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        Map<String, OrderComponent> egglessProductMap = new HashMap<>();
        double componentTotal=0.0;
        String productCodePlusAttribute="",attributeId="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        try{


            //orderProductExtraInfo.getAttributes()  == [Peripheral:10357|Cellophane]
            int indexOfColen=orderProductExtraInfo.getAttributes().indexOf(":");
            int indexOfPipe=orderProductExtraInfo.getAttributes().indexOf("|");
            if(indexOfColen!=-1){
                attributeId=orderProductExtraInfo.getAttributes().substring(indexOfColen+1,indexOfPipe);
            }
            productCodePlusAttribute+="( '"+productCode+"','"+attributeId+"' )";

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select mc.component_code,mc.component_name,mc.type,bc.quantity  "
                + " quantity, mc.componentImage componentImage , mc.component_id , dbc.price ,mc.mod_time   from  "
                + " AA_barcode_to_components bc  join AA_master_components mc on mc.component_id=bc.fk_component_id join "
                + " AA_vendor_to_components dbc on  dbc.fk_component_id=mc.component_id and "
                + " dbc.fk_associate_id=?  where bc.barcode in "+productCodePlusAttribute; //  order by dbc.price * bc.quantity desc


            //            statement =" SELECT  mc.componentImage componentImage ,opci.component_code,mc.component_name,opci.vendor_to_component_price, "
            //                + " opci.products_id,opci.quantity FROM  orders_products_components_info opci join AA_master_components mc ";

            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                calendar.setTime(dateFormat.parse(resultSet.getString("mc.mod_time")));

                OrderComponent orderComponent=new OrderComponent.Builder()
                    .componentCode(resultSet.getString("mc.component_code"))
                    .componentImage(resultSet.getString("componentImage"))
                    .componentName(resultSet.getString("mc.component_name"))
                    .componentPrice(resultSet.getString("dbc.price"))
                    .productId(String.valueOf(productId))
                    .quantity(resultSet.getString("bc.quantity"))
                    .type(resultSet.getString("mc.type"))
                    .timestamp(calendar.getTimeInMillis()+"")
                    .componentId(resultSet.getInt("mc.component_id"))
                    .build();


                if (orderComponent.getComponentName().equalsIgnoreCase("Eggless") && clubEgglessComponentFalg==true)
                {
                    egglessProductMap.put(orderComponent.getProductId(), orderComponent);
                }
                else
                {
                    componentList.add(orderComponent);
                }
                componentTotal+=resultSet.getDouble("dbc.price")*resultSet.getDouble("bc.quantity");
            }

            for (OrderComponent orderComponent : componentList)
            {
                OrderComponent egglessComponent = egglessProductMap.get(orderComponent.getProductId());


                if (egglessComponent != null && (orderComponent.getComponentName().contains("Cake")
                    || orderComponent.getComponentName().contains("cake")))
                {
                    orderComponent.setEggless(true);
                    String productComponentPrice = orderComponent.getComponentPrice();
                    if (productComponentPrice.equals("N/A"))
                    {
                        orderComponent.setComponentPrice(egglessComponent.getComponentPrice());
                    }
                    else
                    {
                        double total = Double.valueOf(productComponentPrice)
                            + Double.valueOf(egglessComponent.getComponentPrice());
                        orderComponent.setComponentPrice(total + "");
                    }
                }
            }
            Collections.sort(componentList,new componentListComparator());
        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return componentTotal;
    }

    public double getComponentListFromComponentInfo(int orderId,List<OrderComponent> componentList , OrderProductExtraInfo orderProductExtraInfo
        ,boolean clubEgglessComponentFalg){
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        Map<String, OrderComponent> egglessProductMap = new HashMap<>();
        double componentTotal=0.0;
        String productCodePlusAttribute="",attributeId="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        try{

            int indexOfColen=orderProductExtraInfo.getAttributes().indexOf(":");
            int indexOfPipe=orderProductExtraInfo.getAttributes().indexOf("|");
            if(indexOfColen!=-1){
                attributeId=orderProductExtraInfo.getAttributes().substring(indexOfColen+1,indexOfPipe);
            }

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="SELECT  * from orders_products_components_info opci join AA_master_components mc on opci.component_id"
                + " = mc.component_id where opci.orders_id = ? and products_id = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,orderProductExtraInfo.getProductId());

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                calendar.setTime(dateFormat.parse(resultSet.getString("mc.mod_time")));
                OrderComponent orderComponent=new OrderComponent.Builder()
                    .componentCode(resultSet.getString("opci.component_code"))
                    .componentImage(resultSet.getString("mc.componentImage"))
                    .componentName(resultSet.getString("mc.component_name"))
                    .componentPrice(resultSet.getString("opci.vendor_to_component_price"))
                    .productId(String.valueOf("opci.products_id"))
                    .quantity(resultSet.getString("opci.quantity"))
                    .type(resultSet.getString("mc.type"))
                    .timestamp(calendar.getTimeInMillis()+"")
                    .componentId(resultSet.getInt("opci.component_id"))
                    .build();


                if (orderComponent.getComponentName().equalsIgnoreCase("Eggless") && clubEgglessComponentFalg==true)
                {
                    egglessProductMap.put(orderComponent.getProductId(), orderComponent);
                }
                else
                {
                    componentList.add(orderComponent);
                }
                componentTotal+=resultSet.getDouble("opci.vendor_to_component_price")*resultSet.getDouble("opci.quantity");
            }

            for (OrderComponent orderComponent : componentList)
            {
                OrderComponent egglessComponent = egglessProductMap.get(orderComponent.getProductId());


                if (egglessComponent != null && (orderComponent.getComponentName().contains("Cake")
                    || orderComponent.getComponentName().contains("cake")))
                {
                    orderComponent.setEggless(true);
                    String productComponentPrice = orderComponent.getComponentPrice();
                    if (productComponentPrice.equals("N/A"))
                    {
                        orderComponent.setComponentPrice(egglessComponent.getComponentPrice());
                    }
                    else
                    {
                        double total = Double.valueOf(productComponentPrice)
                            + Double.valueOf(egglessComponent.getComponentPrice());
                        orderComponent.setComponentPrice(total + "");
                    }
                }
            }
            Collections.sort(componentList,new componentListComparator());
        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return componentTotal;
    }

    public Order gerOrderOnly(int orderId,int fkassociateId,boolean forAdminPanelOrNot){

        Connection connection = null;
        ResultSet resultSet = null;
        String statement,orderInstr="",orderSource,orderOccasion;
        PreparedStatement preparedStatement = null;
        Order order=null;
        int addressType=0;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            if(forAdminPanelOrNot==true){
                statement="select o.*,( case when  ab.address_type is not null then ab.address_type else 0 end )  as "
                    + " address_type,oc.occasion_name as occasion, a.associate_name as associate_name  from orders as o left join orders_temp ot on o.orders_temp_id=ot.orders_temp_id "
                    + " left join address_book ab on  ab.address_book_id=ot.address_book_id left join orders_occasions "
                    + " as oc on o.orders_occasionid = oc.occasion_id left join associate a on a.associate_id = o.fk_associate_id "
                    + " where o.orders_id = ? limit 1";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,orderId);
            }else {
                statement="select o.*,group_concat(vi.instruction_msg SEPARATOR ' \n ') as instruction_msg, "
                    + " ( case when  ab.address_type is not null then ab.address_type else 0 end )  as address_type, "
                    + " oc.occasion_name as occasion, a.associate_name as associate_name "
                    + " from orders as o left join orders_temp ot on o.orders_temp_id=ot.orders_temp_id "
                    + " left join address_book ab on  ab.address_book_id=ot.address_book_id left join vendor_instructions as vi ON "
                    + " o.orders_id=vi.orders_id left join orders_occasions as oc on o.orders_occasionid = oc.occasion_id "
                    + " left join associate a on a.associate_id = o.fk_associate_id "
                    + " where o.orders_id = ?  and  vi.associate_id = ? limit 1";
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,orderId);
                preparedStatement.setInt(2,fkassociateId);
            }


            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();

            Map<Integer,String> addressTypeMap=new HashMap<>();
            addressTypeMap.put(0,"Home");
            addressTypeMap.put(1,"Office");
            addressTypeMap.put(2,"Others");

            List<OrdersProducts> ordersProductsList=new ArrayList<>();

            if(resultSet.next()){
                addressType=resultSet.getInt("address_type");
                if(forAdminPanelOrNot==false){
                    orderInstr=resultSet.getString("instruction_msg")==null ? "":resultSet.getString("instruction_msg");
                }
                orderSource=resultSet.getString("associate_name");
                orderOccasion=resultSet.getString("occasion");
                order=new Order.Builder()
                    .orderId(resultSet.getInt("orders_id"))
                    .customerId(resultSet.getLong("customers_id"))
                    .customersSalute(resultSet.getString("customers_salute"))
                    .customersName(resultSet.getString("customers_name"))
                    .customersStreetAddress(resultSet.getString("customers_street_address"))
                    .customersStreetAddress2(resultSet.getString("customers_street_address2"))
                    .customersCity(resultSet.getString("customers_city"))
                    .customersPostcode(resultSet.getString("customers_postcode"))
                    .customersCountry(resultSet.getString("customers_country"))
                    .customerstelephone(resultSet.getString("customers_telephone"))
                    .customersEmail(resultSet.getString("customers_email_address"))
                    .customersMobile(resultSet.getString("customers_mobile"))
                    .deliverySalute(resultSet.getString("delivery_salute"))
                    .deliveryName(resultSet.getString("delivery_name"))
                    .deliveryStreetAddress(resultSet.getString("delivery_street_address"))
                    .deliveryCity(resultSet.getString("delivery_city"))
                    .deliveryPostcode(resultSet.getString("delivery_postcode"))
                    .deliveryState(resultSet.getString("delivery_state"))
                    .deliveryCountry(resultSet.getString("delivery_country"))
                    .deliveryEmail(resultSet.getString("delivery_email_address"))
                    .deliveryMobile(resultSet.getString("delivery_mobile"))
                    .datePurchased(resultSet.getString("date_purchased"))
                    .ordersStatus(resultSet.getString("orders_status"))
                    .commments(resultSet.getString("comments"))
                    .delivery_instruction(resultSet.getString("delivery_instruction"))
                    .fkAssociateId(resultSet.getInt("fk_associate_id"))
                    .themeId(resultSet.getInt("themeid"))
                    .ordersOccasionId(resultSet.getInt("orders_occasionid"))
                    .orderInstruction(orderInstr)
                    .addressType(addressTypeMap.get(addressType))
                    .orderSource(orderSource)
                    .orderOccasion(orderOccasion)
                    .orderProducts(ordersProductsList)
                    .build();
            }
        }
        catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }


        return order;
    }

    public boolean saveSlaCodes(int orderId,int orderProductId,String status,int slaCode,boolean deliveryStatus){
        boolean result=false;
        Connection connection = null;
        String statement=null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            if(deliveryStatus==false){
                if(status.equals("Processed")){
                    statement = "update orders_products as op set op.sla_code = ? ,op.sla_code1 = ?  where  op.orders_id=? and "
                        + " op.orders_products_id = ? ";
                }else if(status.equals("Confirmed")){
                    statement = "update orders_products as op set op.sla_code = ? ,op.sla_code2 = ?  where  op.orders_id=? and "
                        + " op.orders_products_id = ? ";
                }else if(status.equalsIgnoreCase("Shipped") && deliveryStatus == false){
                    statement = "update orders_products as op set op.sla_code = ? ,op.sla_code3 = ?  where  op.orders_id=? and "
                        + " op.orders_products_id = ? ";
                }
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1,slaCode);
                preparedStatement.setInt(2,slaCode);
                preparedStatement.setInt(3,orderId);
                preparedStatement.setInt(4,orderProductId);
                logger.debug("sql query while saving sla codes "+preparedStatement);
                Integer rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated>0){
                    result=true;
                }
                else {
                    result=false;
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

    public static String getDeliverWhen(String deliverDate) throws ParseException
    {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
            Date tomorrowDate = DateUtils.addDays(todayDate, 1);
            Date futureDate = DateUtils.addDays(todayDate, 2);
            Date deliveryDate = df.parse(deliverDate);

            if (deliveryDate.getTime() < todayDate.getTime())
            {
                return "past";
            }
            else if (deliveryDate.getTime() == todayDate.getTime())
            {
                return "today";
            }
            else if (deliveryDate.getTime() == tomorrowDate.getTime())
            {
                return "tomorrow";
            }
            else if (deliveryDate.getTime() >= futureDate.getTime())
            {
                return "future";
            }
        }
        catch (ParseException e)
        {
        }
        return "";
    }

    /*
    New to Confirmed -

    Orders received before 8PM - 45mins from order time - 1 , 101
    Orders received after 8PM - By 9AM next day -  2 , 102

    Confirmed to Out for Delivery -

    Fixed Date Orders with delivery date as today -  Orders received before 8PM yesterday - 2PM today   - 21  , 201
    Orders received after 8PM yesterday - By 7PM today  - 22,202
    Fixed Time Orders with delivery date as today -By slot beginning time + 1 hour  - 23 , 203
    Mid Night Delivery with delivery date as yesterday -By 9AM today - 24 , 204

    Out for Delivery to Delivered

    Fixed Date Orders with delivery date as today -Orders received before 8PM yesterday - 2:45PM today   - 41 , 401
    Orders received after 8PM yesterday - By 7:45PM today  - 42 , 402
    Fixed Time Orders with delivery date as today -By slot beginning time + 2 hour   - 43 , 403
    Mid Night Delivery with delivery date as yesterday -By 9AM today - 44 , 404
    */


    public static boolean isSLASatisfied(int slaCode)
    {
        boolean slaSatisfied = false;

        switch (slaCode)
        {
            case 1:
            case 2:
            case 21:
            case 22:
            case 23:
            case 24:
            case 41:
            case 42:
            case 43:
            case 44:
            case 6:
                slaSatisfied = true;
                break;
        }
        return slaSatisfied;
    }

    public static boolean isHighAlertActionRequired(int slaCode)
    {
        boolean alertActionRequired = false;

        switch (slaCode)
        {
            case 100:
            case 101:
            case 102:
            case 201:
            case 202:
            case 203:
            case 204:
            case 401:
            case 402:
            case 403:
            case 404:
            case 601:
                alertActionRequired = true;
                break;
        }
        return alertActionRequired;
    }
    public static  String getTimeWhenColorChangesforOrderProduct(int slaCode){
        String result;
        if( slaCode == 21 || slaCode == 201 ){
            result="10:00 hrs - 14:00 hrs";
        }else if( slaCode == 202 || slaCode == 22 ){
            result="10:00 hrs - 19:00 hrs";
        }else if(slaCode == 41 || slaCode == 401){
            result="10:00 hrs - 14:45 hrs";
        }else if(slaCode == 42 || slaCode == 402){
            result="10:00 hrs - 19:45 hrs";
        }else {
            result="";
        }
        return result;
    }

    class componentListComparator implements Comparator<OrderComponent>{

        @Override public int compare(OrderComponent orderComponent1, OrderComponent orderComponent2)
        {
            if((Double.parseDouble(orderComponent1.getQuantity())*Double.parseDouble(orderComponent1.getComponentPrice())) >=
                (Double.parseDouble(orderComponent2.getQuantity())*Double.parseDouble(orderComponent2.getComponentPrice()))){
                return -1;
            } else {
                return 1;
            }
        }
    }
}
