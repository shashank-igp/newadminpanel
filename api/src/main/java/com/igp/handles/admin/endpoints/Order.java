package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Order.OrderMapper;
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
    public HandleServiceResponse assignReassignOrder(@QueryParam("action")String action , @QueryParam("orderId") int orderId,
        @QueryParam("fkAssociateId") int vendorId,@QueryParam("orderProductId") int orderProductId
                                                ,@DefaultValue("0")@QueryParam("orderProductIds") String allOrderProductIdList){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        int result;
        List<com.igp.handles.vendorpanel.models.Order.Order> orderList=new ArrayList<>();

        try{

            logger.debug("step-1 assignReassignOrder with "+action);

             result=orderMapper.assignReassignOrder(action,orderId,orderProductId,vendorId,allOrderProductIdList,orderList,handleServiceResponse);
            if(result==1){
//                handleServiceResponse.setResult(orderList);
            }else if(result==2){
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("could not assign the order to this vendor because all required components are not available !!");
            }else if(result==3){
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("could not assign the order to this vendor because already assigned to this vendor !!");
            }
            else{
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("could not assign the order to this vendor please try again !!");
            }
        }catch (Exception exception){
            logger.error("error while assignReassignOrder",exception);
        }

        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/orderPriceChanges")
    public HandleServiceResponse orderPriceChanges(@QueryParam("orderId")int orderId,@QueryParam("orderproductId")
        int orderProductId,@QueryParam("componentId") int componentId,@QueryParam("componentPrice") Double componentPrice,
        @QueryParam("shippingCharge") Double shippingCharge,
        @DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){

        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        boolean result=false;
        List<com.igp.handles.vendorpanel.models.Order.Order> orderList=new ArrayList<>();
        try{
            result=orderMapper.orderPriceChanges(orderId,orderProductId,componentId,componentPrice,shippingCharge);
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
    public HandleServiceResponse deliveryDetailChanges(@QueryParam("orderId")int orderId,@QueryParam("orderProductId") int orderProductId
    ,@QueryParam("deliveryDate") String deliveryDate,@QueryParam("deliveryTime")String deliveryTime,@QueryParam("deliveryType") int deliveryType ,
        @DefaultValue("0")@QueryParam("orderProductIds") String orderProductIdList){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        boolean result=false;
        try{
            result=orderMapper.deliveryDetailChanges(orderId,orderProductId,deliveryDate,deliveryTime,deliveryType
                ,handleServiceResponse,orderProductIdList);
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
            Map<String,String> orderLog=new HashMap<>();
            orderLog.put("logs",orderMapper.getOrderLog(orderId));
            handleServiceResponse.setResult(orderLog);

        }catch (Exception exception){
            logger.error("error while getting OrderLog",exception);
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
    public HandleServiceResponse cancelOrder(@QueryParam("orderId") int orderId,@QueryParam("orderProductId")int orderProductId
                                        ,@QueryParam("comment")String comment){
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        boolean result=false;
        try{
            result=orderMapper.cancelOrder(orderId,orderProductId,comment);
            if(result){
                handleServiceResponse.setResult(result);
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

}
