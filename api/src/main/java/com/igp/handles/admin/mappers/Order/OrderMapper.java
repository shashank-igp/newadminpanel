package com.igp.handles.admin.mappers.Order;

import com.igp.handles.vendorpanel.models.Order.Order;
import com.igp.handles.vendorpanel.models.Order.OrderComponent;
import com.igp.handles.vendorpanel.models.Order.OrderProductExtraInfo;
import com.igp.handles.vendorpanel.models.Order.OrdersProducts;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.utils.Order.OrderUtil;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by shanky on 22/1/18.
 */
public class OrderMapper {
    private static final Logger logger = LoggerFactory.getLogger(OrderMapper.class);
    public List<Order> getOrderByStatusDate(String category ,String subCategory, Date date,
        String orderAction, String section, boolean isfuture){

        List<Order> orders=new ArrayList<>();
        OrderUtil orderUtilVendorPanel=new OrderUtil();
        com.igp.handles.vendorpanel.mappers.Order.OrderMapper orderMapperVendorPanel=new com.igp.handles.vendorpanel.mappers.Order.OrderMapper();
        String status="",slaClause=null;
        int fkassociateId=-1;
        Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap = new HashMap<>();
        try{

            if(category.equals("unAssigned")){
                if(subCategory.equals("notAlloted")){
                    status="Processed";
                    fkassociateId=72;
                }else if(subCategory.equals("processing")) {
                    status="Processing";
                    fkassociateId=72;
                }
            }else if(category.equals("notConfirmed")){
                if(subCategory.equals("pending")){
                    status="Processed";
                    slaClause="(100,101,102)";
                }else if(subCategory.equals("total")){
                    status="Processed";
                }
            }else if(category.equals("notShipped")){
                if(subCategory.equals("pending")){
                    status="Confirmed";
                    slaClause="(100,201,202,203,204)";
                }else if(subCategory.equals("total")){
                    status="Confirmed";
                }
            }else if(category.equals("notDelivered")){
                if(subCategory.equals("pending")){
                    status="OutForDelivery";
                    slaClause="(100,401,402,403,404)";
                }else if(subCategory.equals("total")){
                    status="OutForDelivery";
                }
            }

            List<OrdersProducts> orderProductList = orderUtilVendorPanel.getOrderProductsByStatusDate("1",fkassociateId,status,date,
                section,  isfuture,ordersProductExtraInfoMap,true,slaClause);
            orders=orderMapperVendorPanel.prepareOrders(orderAction,orderProductList,ordersProductExtraInfoMap,"",true);

        }catch (Exception exception){
            logger.error("error while getOrderByStatusDate",exception);
        }

        return orders;
    }
    public int assignReassignOrder(String action,int orderId,int orderProductId,int vendorId,String allOrderProductIdList,
        List<Order> orderList,HandleServiceResponse handleServiceResponse){
        int result=0;
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();

        String restOrderProductIdList="";
        try{
            String[] orderProductIds=allOrderProductIdList.split(",");
            for(int i=0;i<orderProductIds.length;i++){
                if(!orderProductIds[i].equals(String.valueOf(orderProductId))){
                        restOrderProductIdList+=orderProductIds[i]+",";
                }
            }
            if(!restOrderProductIdList.equals("")){
                restOrderProductIdList=restOrderProductIdList.substring(0,restOrderProductIdList.length()-1);
            }

            Order order=orderUtil.getOrderRelatedInfo(orderId,orderProductId);
            OrdersProducts ordersProducts=order.getOrderProducts().get(0);

            logger.debug("step-2 assignReassignOrder with orderProductId "+orderProductId+" and restOrderProductIdList "+restOrderProductIdList);

            if(action.equalsIgnoreCase("assign")){
                result=orderUtil.assignOrderToVendor(orderId,orderProductId,vendorId,order);
                if(result==1){
                    if(!restOrderProductIdList.equals("")){
                        orderList=getOrder(orderId,restOrderProductIdList);
                    }
                }
            }else if(action.equalsIgnoreCase("reassign")) {


                result=orderUtil.reassignOrderToVendor(orderId,orderProductId,vendorId,order);
                if(result==1){
                    if(ordersProducts.getOrdersProductStatus().equals("Processed")
                        &&!ordersProducts.getFkAssociateId().equals("72")){

                        if(!restOrderProductIdList.equals("")){
                            orderList=getOrder(orderId,String.valueOf(orderProductId));
                            orderList = mergeOrderList(orderList, getOrder(orderId,restOrderProductIdList));
                        }else {
                            orderList=getOrder(orderId,String.valueOf(orderProductId));
                        }
                    }else {
                        if(!restOrderProductIdList.equals("")){
                            orderList=getOrder(orderId,restOrderProductIdList);
                        }
                    }
                }
            }
            handleServiceResponse.setResult(orderList);

        }catch (Exception exception){
            logger.error("error while assignReassignOrder",exception);
        }

        return result;
    }
    public boolean orderPriceChanges(int orderId,int orderProductId,int componentId,Double componentPrice,Double shippingCharge){
        boolean result=false;
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();
        Double vendorPrice=null;
        int productId=0;
        try {
            productId=orderUtil.getProductId(orderProductId);
            OrderComponent orderComponent=orderUtil.getOrderComponent(orderId,productId,componentId);

            if(componentPrice!=null && orderComponent != null){
                orderUtil.updateComponentPriceOrderLevel(orderId,productId,componentId,componentPrice);
                vendorPrice = abs(Double.valueOf(orderComponent.getQuantity())*Double.valueOf(orderComponent.getComponentPrice())
                    - Double.valueOf(orderComponent.getQuantity())*componentPrice ) ;
            }

            result=orderUtil.updateVendorAssignPrice(orderId,productId,vendorPrice,shippingCharge,orderProductId);

        }catch (Exception exception){
            logger.error("error while orderComponentPriceChange",exception);
        }
        return result;
    }
    public boolean deliveryDetailChanges(int orderId,int orderProductId,String deliveryDate,String deliveryTime,int deliveryType,
        HandleServiceResponse handleServiceResponse,String orderProductIdList){
        boolean result=false;
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();
        int productId=0;
        List<Order> orderList=new ArrayList<>();
        String restOrderProductIdList="";
        try{
            String[] orderProductIds=orderProductIdList.split(",");
            for(int i=0;i<orderProductIds.length;i++){
                if(!orderProductIds[i].equals(String.valueOf(orderProductId))){
                    restOrderProductIdList+=orderProductIds[i]+",";
                }
            }
            if(!restOrderProductIdList.equals("")){
                restOrderProductIdList=restOrderProductIdList.substring(0,restOrderProductIdList.length()-1);
            }
            productId=orderUtil.getProductId(orderProductId);
            result=orderUtil.updateDeliveryDetails(orderId,orderProductId,productId,deliveryDate,deliveryTime,deliveryType);
            if(result){
                if(deliveryDate!=null){
                    if(!restOrderProductIdList.equals("")){
                        orderList=getOrder(orderId,restOrderProductIdList);
                    }
                }else{
                    if(!restOrderProductIdList.equals("")){
                        orderList=getOrder(orderId,String.valueOf(orderProductId));
                        orderList=mergeOrderList(orderList,getOrder(orderId,restOrderProductIdList));
                    }else {
                        orderList=getOrder(orderId,String.valueOf(orderProductId));
                    }
                }
                handleServiceResponse.setResult(orderList);
            }
        }catch (Exception exception){
            logger.error("error while deliveryDetailChanges",exception);
        }
        return result;
    }
    public String getOrderLog(int orderId){
        String logs="";
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();
        try {
            logs=orderUtil.getOrderLog(orderId);
        }catch (Exception exception){
            logger.error("error while getting OrderLog",exception);

        }
        return logs;
    }
    public List<Order> getOrder(int orderId,String orderProductIdList){
        List<Order> orders = new ArrayList<>();
        OrderUtil orderUtil=new OrderUtil();
        com.igp.handles.vendorpanel.mappers.Order.OrderMapper orderMapper=new com.igp.handles.vendorpanel.mappers.Order.OrderMapper();

        Map<Integer, OrderProductExtraInfo> ordersProductExtraInfoMap = new HashMap<>();
        try{
            List<OrdersProducts> orderProductList = orderUtil.getOrderProducts("1", orderId, "",ordersProductExtraInfoMap,orderProductIdList,true);
            orders=orderMapper.prepareOrders("all",orderProductList,ordersProductExtraInfoMap,"",true);

        }
        catch (Exception e){
            logger.error("Exception while generating orders",e);
        }
        return orders;
    }
    public boolean cancelOrder(int orderId,int orderProductId,String comment,HandleServiceResponse handleServiceResponse,String orderProductIdList){
        boolean result=false;
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new com.igp.handles.admin.utils.Order.OrderUtil();
        String restOrderProductIdList="";
        List<Order> orderList=new ArrayList<>();
        try {
            String[] orderProductIds=orderProductIdList.split(",");
            for(int i=0;i<orderProductIds.length;i++){
                if(!orderProductIds[i].equals(String.valueOf(orderProductId))){
                    restOrderProductIdList+=orderProductIds[i]+",";
                }
            }
            if(!restOrderProductIdList.equals("")){
                restOrderProductIdList=restOrderProductIdList.substring(0,restOrderProductIdList.length()-1);
                orderList=getOrder(orderId,restOrderProductIdList);
            }
            handleServiceResponse.setResult(orderList);
            result=orderUtil.cancelOrder(orderId,orderProductId,comment);
        }catch (Exception exception){
            logger.error("error while cancelling ",exception);
        }
        return result;
    }
    public List<Order> mergeOrderList(List<Order> orderList,List<Order> orderList1){
        return ListUtils.union(orderList,orderList1);
    }
}
