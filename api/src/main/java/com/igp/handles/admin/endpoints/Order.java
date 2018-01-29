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
import java.util.Date;
import java.util.List;

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

        List<com.igp.handles.vendorpanel.models.Order.Order> orders= orderMapper.getOrderByStatusDate(category,subCategory,date1,orderAction,section,isfuture);
        response.addHeader("token",request.getHeader("token"));
        handleServiceResponse.setResult(orders);

        return handleServiceResponse;
    }

    @POST
    @Path("/v1/admin/handels/assignReassignOrder")
    public HandleServiceResponse assignReassignOrder(@QueryParam("action")String action , @QueryParam("orderId") int orderId,
        @QueryParam("fkAssociateId") int vendorId,@QueryParam("orderProductId") int orderProductId){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        OrderMapper orderMapper=new OrderMapper();
        try{
            if(orderMapper.assignReassignOrder(action,orderId,orderProductId,vendorId)){
                handleServiceResponse.setResult(true);
            }else{
                handleServiceResponse.setError(true);
                handleServiceResponse.setResult(false);
                handleServiceResponse.setErrorMessage("could not assign the order to this vendor please try again !!");
            }
        }catch (Exception exception){
            logger.error("error while assignReassignOrder",exception);
        }

        return handleServiceResponse;
    }


}
