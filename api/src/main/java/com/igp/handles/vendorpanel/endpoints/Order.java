package com.igp.handles.vendorpanel.endpoints;

import com.igp.handles.admin.models.Order.OrderLogModel;
import com.igp.handles.vendorpanel.mappers.Order.OrderMapper;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igp.handles.vendorpanel.mappers.Order.OrderMapper.doUpdateOrderProductsStatus;


/**
 * Created by shanky on 8/8/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Order {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);
    @GET
    @Path("/v1/handels/getOrderByStatusDate")
    public HandleServiceResponse getOrderByStatusDate (@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                        @DefaultValue("1") @QueryParam("scopeId") String scopeId,
                                                       @QueryParam("fkassociateId") int fkAssociateId,
                                                        @QueryParam("status") String status,@DefaultValue("today")
                                                        @QueryParam("section") String section,@DefaultValue("all")
                                                        @QueryParam("orderAction") String orderAction,
                                                        @QueryParam("isfuture") boolean isfuture,
                                                        @QueryParam("date") String date) throws ParseException
    {
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        OrderMapper orderMapper = new OrderMapper();
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
            case "specific" :
                date1 = new Date(Long.valueOf(date));
                break;
            default:
                break;
        }

        List<com.igp.handles.vendorpanel.models.Order.Order> orders=orderMapper.getOrderByStatusDate(scopeId,fkAssociateId,status,date1,
                                                                                        orderAction,section,isfuture);
        response.addHeader("token",request.getHeader("token"));
        handleServiceResponse.setResult(orders);
        return handleServiceResponse;
    }


    @POST
    @Path("/v1/handels/doUpdateOrderStatus")
    public  HandleServiceResponse doUpdateOrderStatus(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                        @QueryParam("orderId") int orderId, @QueryParam("fkAssociateId") String fkAssociateId,
                                                        @QueryParam("status") String status, @QueryParam("orderProductIds")String orderProductsIds,
                                                        @QueryParam("rejectionMessage") String rejectionMessage,
                                                        @QueryParam("recipientInfo") String recipientInfo,
                                                        @QueryParam("comments") String commentsDelivered,
                                                        @QueryParam("recipientName") String recipientName,
                                                        @DefaultValue("0") @QueryParam("rejectionType") int rejectionType)
    {
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        Boolean ifSucessfull=false;
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        ifSucessfull=doUpdateOrderProductsStatus(orderId,fkAssociateId,status,orderProductsIds,recipientInfo,
                    rejectionMessage,commentsDelivered,recipientName,rejectionType,ipAddress,userAgent);

        handleServiceResponse.setResult(ifSucessfull);
        response.addHeader("token",request.getHeader("token"));
        return  handleServiceResponse;

    }


    @GET
    @Path("/v1/handels/getOrder")
    public HandleServiceResponse getOrder(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                            @DefaultValue("1") @QueryParam("scopeId") String scopeId,   @QueryParam("orderId") int orderId,
                                          @QueryParam("fkassociateId") String fkAssociateId,@DefaultValue("0") @QueryParam("orderProductIds") String orderProductIds) {
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        OrderMapper orderMapper = new OrderMapper();
        List<com.igp.handles.vendorpanel.models.Order.Order> orders = orderMapper.getOrder(scopeId, orderId, fkAssociateId,orderProductIds);


        response.addHeader("token",request.getHeader("token"));
        handleServiceResponse.setResult(orders);
        return handleServiceResponse;
    }
    @GET
    @Path("/v1/handels/getOrderLog")
    public HandleServiceResponse getOrderLog(@QueryParam("orderId") int orderId,@QueryParam("fkAssociateId") String fkAssociateId){ // here fkAssociateId instead of fkassociateId   just for quick bug fix
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        com.igp.handles.admin.mappers.Order.OrderMapper orderMapper=new com.igp.handles.admin.mappers.Order.OrderMapper();
        try{
            Map<String,List<OrderLogModel>> orderLog=new HashMap<>();
            orderLog.put("logs",orderMapper.getOrderLog(orderId,"message",fkAssociateId));
            handleServiceResponse.setResult(orderLog);

        }catch (Exception exception){
            logger.error("error while getting OrderLog",exception);
        }

        return handleServiceResponse;
    }
    @POST
    @Path("/v1/handels/updateOrderStatusInBulk")
    public  HandleServiceResponse updateOrderStatusInBulk(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                            @QueryParam("orderIdList") String orderIdList,
                                                            @QueryParam("status") String status,
                                                            @QueryParam("recipientName") String recipientName,
                                                            @DefaultValue("0") @QueryParam("rejectionType") int rejectionType)
    {
        //orderIdList is comma separated list of order Ids
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        List<String> orderIdStatusList=null;
        try{
            String ipAddress=request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            orderIdStatusList=orderMapper.updateOrderStatusInBulk(orderIdList,recipientName,ipAddress,userAgent);
            if(orderIdStatusList.size()>0){
                handleServiceResponse.setResult(orderIdStatusList);
            }else{
                orderIdStatusList.add("Nothing happend ask shanky for his help :P ");
                handleServiceResponse.setResult(orderIdStatusList);
            }
            response.addHeader("token",request.getHeader("token"));
        }catch (Exception exception){
            logger.error("error while updating order status in bulk",exception);
        }
        return  handleServiceResponse;

    }
}
