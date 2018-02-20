package com.igp.handles.vendorpanel.mappers.Order;

import com.igp.handles.admin.utils.Order.SlaCompliant;
import com.igp.handles.vendorpanel.models.Order.Order;
import com.igp.handles.vendorpanel.models.Order.OrderComponent;
import com.igp.handles.vendorpanel.models.Order.OrderProductExtraInfo;
import com.igp.handles.vendorpanel.models.Order.OrdersProducts;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import com.igp.handles.vendorpanel.utils.FileUpload.UploadUtil;
import com.igp.handles.vendorpanel.utils.Order.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.igp.handles.vendorpanel.utils.Order.OrderStatusUpdateUtil.updateStatusForOrderUtil;

/**
 * Created by shanky on 8/8/17.
 */
public class OrderMapper
{

    private static final Logger logger = LoggerFactory.getLogger(OrderMapper.class);



    public static  Boolean  doUpdateOrderProductsStatus(int orderId,String fkAssociateId,String status,String orderProductsIds,String recipientInfo,
                                                            String rejectionMessage,String commentsDelivered,String recipientName,int rejectionType){
        Boolean isUpdateSucessfull=false;
        isUpdateSucessfull=updateStatusForOrderUtil(orderId,fkAssociateId,status,orderProductsIds,recipientInfo,
                                                rejectionMessage,commentsDelivered,recipientName,rejectionType);
        return  isUpdateSucessfull;
    }

    public List<Order> getOrder(String scopeId, int orderId, String fkAssociateId,String orderProductIds)
    {
        OrderUtil orderUtil = new OrderUtil();
        List<Order> orders = new ArrayList<>();
        Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap = new HashMap<>();

        try{
            List<OrdersProducts> orderProductList = orderUtil.getOrderProducts(scopeId, orderId, fkAssociateId,ordersProductExtraInfoMap,orderProductIds,false);
            orders=prepareOrders("all",orderProductList,ordersProductExtraInfoMap,"",false);

        }
        catch (Exception e){
            logger.error("Exception while generating orders",e);
        }
        return orders;

    }
    public List<Order> getOrderByStatusDate(String scopeId, int fkassociateId, String status, Date date,
                                            String orderAction, String section, boolean isfuture){

        OrderUtil orderUtil = new OrderUtil();
        List<Order> orders=new ArrayList<>();
        Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap = new HashMap<>();
        try{
            List<OrdersProducts> orderProductList = orderUtil.getOrderProductsByStatusDate(scopeId,fkassociateId,status,date,
                section,  isfuture,ordersProductExtraInfoMap,false,null);
            orders=prepareOrders(orderAction,orderProductList,ordersProductExtraInfoMap,status,false);
        }
        catch (Exception e){
            logger.error("Exception while generating orders",e);
        }
        return orders;

    }
    public List<Order> prepareOrders( String orderAction,
                                        List<OrdersProducts> orderProductList,Map<Integer,
                                        OrderProductExtraInfo> ordersProductExtraInfoMap,String status,boolean forAdminPanelOrNot){


        List<Order> orders=new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Order> originalOrderMap = new LinkedHashMap<>();
        Map<String, Order> tempOrderProductsMap = new LinkedHashMap<>();
        // map of orderId vs max shipping charge
        Map<Integer, Double> orderShippingChargeMap = new HashMap<>();
//        Map<String, Integer> orderShippingTypeMap = new HashMap<>();
        Map<String, String> orderShippingTypeMap = new HashMap<>();
        Map<Integer, String> orderProductDeliveryDateMap = new HashMap<>();
        String deliveryDate="";
        OrderUtil orderUtil=new OrderUtil();
        int fkassociateId=0;

        for (OrdersProducts orderProducts : orderProductList){
            try{
                if (orderAction.equals("sla") && !OrderUtil.isSLASatisfied(orderProducts.getSlaCode())
                    || (orderAction.equals("alert")
                    && !OrderUtil.isHighAlertActionRequired(orderProducts.getSlaCode()))) {

                    if(!status.equalsIgnoreCase("Shipped")){
                        continue;
                    }
                }
                OrderProductExtraInfo orderProductExtraInfo = ordersProductExtraInfoMap
                    .get(orderProducts.getOrderProductId());
                if (orderProductExtraInfo == null) {
                    // means standard delivery product so ignore
                    continue;
                }
                fkassociateId=Integer.parseInt(orderProducts.getFkAssociateId());
                String key = orderProducts.getOrderId() + "," + orderProductExtraInfo.getDeliveryDate() + ","
                    + orderProductExtraInfo.getDeliveryType() + ","
                    + orderProductExtraInfo.getDeliveryTime().replaceAll(":|\\shrs", "")+ "," +fkassociateId;

                orderProducts.setSlaFlag(OrderUtil.isSLASatisfied(orderProducts.getSlaCode()));
                orderProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(orderProducts.getSlaCode()));
                if (orderProductExtraInfo.getDeliveryType() == 2) {
                    // prepare map for fixed Time Delivery
                    String shippingKey = orderProducts.getOrderId() + "," + orderProductExtraInfo.getDeliveryDate();
                    String deliveryTimeString = orderShippingTypeMap.get(shippingKey);
//                    Integer deliveryTime=Integer.parseInt(deliveryTimeString.split(" - ")[1]);
                    String data = orderProductExtraInfo.getDeliveryTime().replaceAll(":|\\shrs", "");
                    String dataPart=data;
                    if (dataPart.contains("-"))
                        dataPart = dataPart.substring(0, data.indexOf(" "));
                    int timeHour = Integer.parseInt(dataPart);

                    if (deliveryTimeString == null) {// || (deliveryTime > timeHour)) {
//                        orderShippingTypeMap.put(shippingKey, timeHour);
                        orderShippingTypeMap.put(shippingKey, data);
                    }
                }
                orderProducts.setOrderProductExtraInfo(orderProductExtraInfo);
                String[] vendorAssignPriceArray=orderUtil.getOrderProductVendorPrice(fkassociateId,orderProducts.getOrderId(),String.valueOf(orderProducts.getProductId()));

                deliveryDate=dateFormat.format(orderProductExtraInfo.getDeliveryDate());
                orderProductDeliveryDateMap.put(orderProducts.getOrderProductId(), deliveryDate);
                orderProducts.setVendorPrice((int)Double.parseDouble(vendorAssignPriceArray[1]));
                orderProducts.setAssignTime(vendorAssignPriceArray[3]);
                Double shippingCharge = orderShippingChargeMap.get(orderProducts.getOrderId());
                if (shippingCharge == null
                    || (shippingCharge.doubleValue() < Double.parseDouble(vendorAssignPriceArray[0]))) {
                    orderShippingChargeMap.put(orderProducts.getOrderId(), Double.parseDouble(vendorAssignPriceArray[0]));
                }

                List<OrderComponent> componentList = new ArrayList<>();

                if(forAdminPanelOrNot==false){
                    orderProducts.setComponentTotal(orderUtil.getProductComponents(orderProducts.getProductId(),fkassociateId,
                        orderProducts.getProducts_code(),componentList,
                        orderProductExtraInfo,true));
                }else {
                    orderProducts.setComponentTotal(orderUtil.getComponentListFromComponentInfo(orderProducts.getOrderId(),componentList,
                        orderProductExtraInfo,true));
                }



                orderProducts.setPriceAdjustmentPerProduct(orderProducts.getVendorPrice()-orderProducts.getComponentTotal());



                // this is when if we have some price changes
                if(orderProducts.getPriceAdjustmentPerProduct()!=0.0){
                    OrderComponent orderComponent=new OrderComponent.Builder()
                        .componentCode("")
                        .componentImage("dummy.jpg")
                        .componentName("Adjustment")
                        .componentPrice(String.valueOf(orderProducts.getPriceAdjustmentPerProduct()))
                        .productId("")
                        .quantity("1")
                        .type("")
                        .build();
                    componentList.add(orderComponent);
                }
                orderProducts.setComponentList(componentList);

                if (orderProductExtraInfo.getDeliveryType() == 4) {
                    Order order = tempOrderProductsMap.get(key);
                    if (order == null) {
                        order = orderUtil.gerOrderOnly( orderProducts.getOrderId(),fkassociateId,forAdminPanelOrNot);
                        order.getOrderProducts().add(orderProducts);
                        tempOrderProductsMap.put(key, order);
                        logger.debug("adding order product id :" + orderProducts.getOrderProductId());
                    } else {
                        logger.debug("adding order product id  :" + orderProducts.getOrderProductId());
                        order.getOrderProducts().add(orderProducts);
                    }

                } else if (orderProductExtraInfo.getDeliveryType() == 2 || orderProductExtraInfo.getDeliveryType() == 3) {
                    Order order = originalOrderMap.get(key);
                    if (order == null) {
                        order = orderUtil.gerOrderOnly( orderProducts.getOrderId(),fkassociateId,forAdminPanelOrNot);
                        originalOrderMap.put(key, order);
                    }
                    if (orderShippingChargeMap.get(orderProducts.getOrderId()) != null) {
                        order.setVendorDeliveryCharge(orderShippingChargeMap.get(orderProducts.getOrderId()));
                    }
                    order.setDeliverWhen(OrderUtil.getDeliverWhen(deliveryDate));
                    order.getOrderProducts().add(orderProducts);
                    order.setVendorOrderTotal(order.getVendorOrderTotal() + orderProducts.getVendorPrice());
                    order.setOrderNetProductPrice(order.getOrderNetProductPrice()+orderProducts.getVendorPrice());
                    order.setComponentTotal(order.getComponentTotal()+orderProducts.getComponentTotal());
                }


            }
            catch (Exception e){
                logger.error("Exception while generating orders",e);
            }

        }




        for (Map.Entry<String, Order> entry : tempOrderProductsMap.entrySet()) {
            String key = entry.getKey();
            try {
                // orderID , deliveryDate , deliveryType , deliveryTime , fkAssociateId
                String[] data = key.split(",");
                String newKey = "";
                String time = orderShippingTypeMap.get(data[0] + "," + data[1]);

                if (time != null) {
                    newKey = data[0] + "," + data[1] + ",2," + time + "," +data[4];
                }
                Order order=null;
//                for(Map.Entry<String,Order> originalOrderMapEntry:originalOrderMap.entrySet()){
//                    String originalOrderMapKey=originalOrderMapEntry.getKey();
//                    if(originalOrderMapKey.startsWith(newKey)){
//                        order=originalOrderMapEntry.getValue();
//                    }
//                }
                order = originalOrderMap.get(newKey);

                logger.debug("old key -> "+key+" and new key to get order object from  original order map "+newKey);
                if (order != null) {
                    // order = getOrderOnly(scopeId, Integer.parseInt(data[0]));

                    for (OrdersProducts orderProducts : entry.getValue().getOrderProducts()) {
                        order.getOrderProducts().add(orderProducts);
                        order.setVendorOrderTotal(order.getVendorOrderTotal() + orderProducts.getVendorPrice());
                        order.setOrderNetProductPrice(order.getOrderNetProductPrice()+orderProducts.getVendorPrice());
                        order.setComponentTotal(order.getComponentTotal()+orderProducts.getComponentTotal());
                    }
                    originalOrderMap.put(newKey, order);
                } else {
                    order = orderUtil.gerOrderOnly(Integer.parseInt(data[0]),fkassociateId,forAdminPanelOrNot);
                    for (OrdersProducts orderProducts : entry.getValue().getOrderProducts()) {
                        order.getOrderProducts().add(orderProducts);
                        order.setVendorOrderTotal(order.getVendorOrderTotal() + orderProducts.getVendorPrice());
                        order.setOrderNetProductPrice(order.getOrderNetProductPrice()+orderProducts.getVendorPrice());
                        order.setComponentTotal(order.getComponentTotal()+orderProducts.getComponentTotal());
                        order.setDeliverWhen(OrderUtil
                            .getDeliverWhen(orderProductDeliveryDateMap.get(orderProducts.getOrderProductId())));
                    }
                    // order.addOrderProducts(entry.getValue());
                    if (order != null) {
                        order.setVendorDeliveryCharge(orderShippingChargeMap.get(order.getOrderId()));
                    }
                    // order.setVendorOrderTotal(order.getVendorOrderTotal() +
                    // entry.getValue().getVendorPrice());
                    originalOrderMap.put(key, order);
                }
                // order.setDeliverWhen(OrderUtil
                // .getDeliverWhen(orderProductDeliveryDateMap.get(entry.getValue().getOrderProductId())));
            } catch (NumberFormatException e) {
                logger.error("Error Key " + key, e);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        Map<Long, Order> sortedOrderMap = new TreeMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        UploadUtil uploadUtil=new UploadUtil();
        for (Map.Entry<String, Order> entry : originalOrderMap.entrySet()) {
            try {
                String[] key = entry.getKey().split(",");
                Order order = entry.getValue();
                // orderID , deliveryDate , deliveryType , deliveryTime
                Long orderId = Long.valueOf(key[0]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(df.parse(key[1]));
                try {
                    if (key.length >=4 && key[3].length() != 0) {
                        key[3]=key[3].split(" - ")[0];
                        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(key[3]) / 100);
                        calendar.add(Calendar.MINUTE, Integer.parseInt(key[3]) % 100);
                    }
                } catch (Exception e) {
                    logger.error("delivery Time Not Present :" + entry.getKey(), e);
                }
                long deliveryTime = calendar.getTimeInMillis() ;/// 86400000l;
                logger.debug("delivery time + orderId  is key for sorted order map "+deliveryTime+" "+orderId.toString()
                );
                
                order.setVendorOrderTotal(order.getVendorOrderTotal() + order.getVendorDeliveryCharge());
                order.setVendorOrderTotal((int)order.getVendorOrderTotal());
                Map<String,List<String>> uploadedFilePath=uploadUtil.getUploadedfilePathFromVpFileUpload(orderId.intValue());
                order.setUploadedFilePath(uploadedFilePath);

                sortedOrderMap.put(Long.valueOf(deliveryTime+orderId.longValue()), order);

                // doing this only for admin/Handel panel
                if(forAdminPanelOrNot){
                    fillSlaFlagsOnOrderProducts(order,forAdminPanelOrNot);
                }
            } catch (Exception e) {
                logger.error("Error in the order :" + entry.getKey(), e);
            }
        }

        return originalOrderMap.values().size() != 0 ? new ArrayList<>(sortedOrderMap.values()) : new ArrayList<>();
    }

    public void fillSlaFlagsOnOrderProducts(Order order,boolean forAdminPanelOrNot ){

        SlaCompliant slaCompliant=new SlaCompliant();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        int slaCode=-12;

        int flagForAdminPanel=forAdminPanelOrNot==true ? 1 : 0;
        try{
            for(OrdersProducts ordersProducts:order.getOrderProducts()){
                OrderDetailsPerOrderProduct orderDetailsPerOrderProduct = new OrderDetailsPerOrderProduct();

                orderDetailsPerOrderProduct.setAssignTime(ordersProducts.getAssignTime());
                orderDetailsPerOrderProduct.setPurchasedTime(formatter.format(order.getDatePurchased()));
                orderDetailsPerOrderProduct.setDeliveryDate(ordersProducts.getOrderProductExtraInfo().getDeliveryDate());
                orderDetailsPerOrderProduct.setShippingType(String.valueOf(ordersProducts.getOrderProductExtraInfo().getDeliveryType()));
                orderDetailsPerOrderProduct.setDeliveryStatus(ordersProducts.getDeliveryStatus()==1 ? true:false);
                orderDetailsPerOrderProduct.setOrderProductStatus(ordersProducts.getOrdersProductStatus());
                orderDetailsPerOrderProduct.setDeliveryTime(ordersProducts.getOrderProductExtraInfo().getDeliveryTime());

                slaCode = slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,flagForAdminPanel);

                logger.debug("slacode  on layer "+slaCode);

                ordersProducts.setSlaFlag(OrderUtil.isSLASatisfied(slaCode));
                ordersProducts.setAlertFlag(OrderUtil.isHighAlertActionRequired(slaCode));
            }
        }catch (Exception exception){
            logger.error("error occured while calculating sla codes on the go");
        }

    }

}
