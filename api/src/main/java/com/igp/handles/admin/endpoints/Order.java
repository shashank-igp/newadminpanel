package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Order.OrderMapper;
import com.igp.handles.admin.models.Order.OrderLogModel;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.*;

/**
 * Created by shanky on 22/1/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Order {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);
    @GET
    @Path("/v1/admin/handels/getOrderByStatusDate")
    public HandleServiceResponse getOrderByStatusDate (@Context HttpServletResponse response,@Context HttpServletRequest request,
        @DefaultValue("1") @QueryParam("scopeId") String scopeId,
        @QueryParam("category") String category,
        @QueryParam("subcategory") String subCategory,@DefaultValue("today")
    @QueryParam("section") String section,@DefaultValue("all")
    @QueryParam("orderAction") String orderAction,
        @QueryParam("isfuture") boolean isfuture,
        @QueryParam("date") String date) throws ParseException{
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();

        Date date1 = new Date();
        //        Date date1= DateUtils.truncate(dateFormat.parse("2017-01-01"), Calendar.DAY_OF_MONTH);
        switch (section)
        {
            case "past":
                date1 = DateUtils.addDays(date1, -7);
                break;
            case "today":
                // date = new Date();
                break;
            case "tomorrow":
                date1 = DateUtils.addDays(date1, 1);
                break;
            case "future":
                date1 = DateUtils.addDays(date1, 2);
                isfuture = true;
                break;
            case "specific":
                date1 = new Date(Long.valueOf(date));
                break;
            default:
                break;
        }
        if(category.equals("notShipped")){
            section="tillToday";
            date1 = DateUtils.addDays(date1, -7);
        }

        List<com.igp.handles.vendorpanel.models.Order.Order> orders= orderMapper.getOrderByStatusDate(category,subCategory,date1,orderAction,section,isfuture);
        response.addHeader("token",request.getHeader("token"));
        handleServiceResponse.setResult(orders);

        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/assignReassignOrder")
    public HandleServiceResponse assignReassignOrder(@Context HttpServletRequest request,@QueryParam("action")String action , @QueryParam("orderId") int orderId,
        @QueryParam("fkAssociateId") int vendorId,@QueryParam("orderProductId") String orderProductIdString
        ,@DefaultValue("0")@QueryParam("orderProductIds") String allOrderProductIdList){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        int result;
        List<com.igp.handles.vendorpanel.models.Order.Order> orderList=new ArrayList<>();

        try{
            //orderProductIdString == comma separated orderProductIds which we have to assign to a vendor
            logger.debug("step-1 assignReassignOrder with "+action);
            orderMapper.assignReassignOrder(action,orderId,orderProductIdString,vendorId,allOrderProductIdList,orderList,handleServiceResponse,ipAddress,userAgent);
        }catch (Exception exception){
            logger.error("error while assignReassignOrder",exception);
        }

        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/orderPriceChanges")
    public HandleServiceResponse orderPriceChanges(@Context HttpServletRequest request,@QueryParam("orderId")int orderId,@QueryParam("orderProductId")
        int orderProductId,@QueryParam("componentId") Integer componentId,@QueryParam("componentPrice") Double componentPrice,
        @QueryParam("shippingCharge") Double shippingCharge,
        @DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){

        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result=false;
        List<com.igp.handles.vendorpanel.models.Order.Order> orderList=new ArrayList<>();
        try{
            result=orderMapper.orderPriceChanges(orderId,orderProductId,componentId,componentPrice,shippingCharge,ipAddress,userAgent);
            if(result==false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("some technical error occured while updating price try again !!");
            }else {
                orderList=orderMapper.getOrder(orderId,orderProductIdList);
                handleServiceResponse.setResult(orderList);
            }
        }catch (Exception exception){
            logger.error("error while orderComponentPriceChange",exception);
        }

        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/deliveryDetailChanges")
    public HandleServiceResponse deliveryDetailChanges(@Context HttpServletRequest request,@QueryParam("orderId")int orderId,@QueryParam("orderProductId") int orderProductId
        ,@QueryParam("deliveryDate") String deliveryDate,@QueryParam("deliveryTime")String deliveryTime,@QueryParam("deliveryType") int deliveryType ,
        @DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result=false;
        try{
            result=orderMapper.deliveryDetailChanges(orderId,orderProductId,deliveryDate,deliveryTime,deliveryType
                ,handleServiceResponse,orderProductIdList,ipAddress,userAgent);
            if(result==false){
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("some technical error occured while updating deliveryDetails try again !!");
            }else {
                //                handleServiceResponse.setResult(result);
            }

        }catch (Exception exception){
            logger.error("error while deliveryDetailChanges",exception);
        }

        return handleServiceResponse;
    }
    @GET
    @Path("/v1/admin/handels/getOrderLog")
    public HandleServiceResponse getOrderLog(@QueryParam("orderId") int orderId){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        try{
            Map<String,List<OrderLogModel>> orderLog=new HashMap<>();
            orderLog.put("logs",orderMapper.getOrderLog(orderId,"all"));
            handleServiceResponse.setResult(orderLog);

        }catch (Exception exception){
            logger.error("error while getting OrderLog Handel panel ",exception);
        }

        return handleServiceResponse;
    }

    @GET
    @Path("/v1/admin/handels/getOrder")
    public HandleServiceResponse getOrder(@Context HttpServletResponse response,@Context HttpServletRequest request,
        @QueryParam("orderId") int orderId,
        @DefaultValue("0") @QueryParam("orderProductIs") String orderProductIdList) {

        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        try{
            handleServiceResponse.setResult(orderMapper.getOrder(orderId,orderProductIdList));
        }catch (Exception exception){
            logger.error("error while getting OrderLog",exception);
        }

        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/cancelOrder")
    public HandleServiceResponse cancelOrder(@Context HttpServletRequest request,@QueryParam("orderId") int orderId,@QueryParam("orderProductId")String orderProductIdString,@QueryParam("comment")String comment,@DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result=false;
        try{
            result=orderMapper.cancelOrder(orderId,orderProductIdString,comment,handleServiceResponse,orderProductIdList,ipAddress,userAgent);
            if(result){
                //                handleServiceResponse.setResult(result);
            }else{
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("some technical error occured while updating order status try again !!");
            }

        }catch (Exception exception){
            logger.error("error while getting OrderLog",exception);
        }

        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/approveDeliveryAttempt")
    public HandleServiceResponse approveDeliveryAttempt(@Context HttpServletRequest request,@QueryParam("orderId") int orderId,@DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        boolean result=false;
        try{
            result=orderMapper.approveDeliveryAttempt(orderId,orderProductIdList,ipAddress,userAgent);
            handleServiceResponse.setResult(result);
        }catch (Exception exception){
            logger.error("error while getting approveDeliveryAttempt ",exception);
        }
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/addVendorInstruction")
    public HandleServiceResponse addVendorInstruction(@Context HttpServletRequest request,@QueryParam("orderId") int orderId,@QueryParam("orderProductId") String orderProductIdString,@QueryParam("fkAssociateId") int fkAssociateId,@QueryParam("message") String instruction){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        boolean result=false;
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Map<String,List<OrderLogModel>> orderLog=new HashMap<>();
        try{
            result=orderMapper.addVendorInstruction(orderId,orderProductIdString,fkAssociateId,instruction,ipAddress,userAgent);
            orderLog.put("logs",orderMapper.getOrderLog(orderId,"all"));
            handleServiceResponse.setResult(orderLog);
        }catch (Exception exception){
            logger.error("error while getting addVendorInstruction ",exception);
        }
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/admin/handels/bulkassign")
    public HandleServiceResponse bulkAssign(@Context HttpServletRequest request,Map<String,Map<String,String>> bulkAssignOrderIdMap){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        OrderMapper orderMapper=new OrderMapper();
        Set<String> responseMap=null;
        try{
            responseMap=orderMapper.bulkAssign(bulkAssignOrderIdMap,ipAddress,userAgent,handleServiceResponse);
            logger.debug("responseMap after bulk assign "+responseMap.toString());
            handleServiceResponse.setResult(true);
        }catch (Exception exception){
            logger.error("error while getting bulkAssign ",exception);
        }
        return handleServiceResponse;
    }
}
