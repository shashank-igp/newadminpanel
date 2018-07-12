package com.igp.handles.vendorpanel.endpoints;

import com.igp.handles.admin.mappers.Order.OrderMapper;
import com.igp.handles.admin.models.Order.OrderLogModel;
import com.igp.handles.vendorpanel.mappers.Vendor.HandlesVendorMapper;
import com.igp.handles.vendorpanel.models.Vendor.VendorCountDetail;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import com.igp.handles.vendorpanel.utils.Vendor.VendorUtil;
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

/**
 * Created by shanky on 8/7/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Vendor {
    private static final Logger logger = LoggerFactory.getLogger(OrderMapper.class);
    @GET
    @Path("/v1/handels/getVendorCountDetail")
    public HandleServiceResponse getVendorCountDetail(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                        @DefaultValue("1") @QueryParam("scopeId") String scopeId,
                                                        @QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("specificDate") String date)
        throws ParseException
    {
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        HandlesVendorMapper handlesVendorMapper=new HandlesVendorMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date specificDate = null;
        try
        {
            specificDate = (date.equals("0") ? null : dateFormat.parse(date));
        }
        catch (ParseException e)
        {
        }
        VendorCountDetail vendorCountDetail=handlesVendorMapper.getVendorCountDetail(scopeId,fkAssociateId,specificDate);

        response.addHeader("token",request.getHeader("token"));
        handleServiceResponse.setResult(vendorCountDetail);
        return handleServiceResponse;
    }
    @POST
    @Path("/v1/handels/vendorissue")
    public HandleServiceResponse saveVendorIssue(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                @QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("orderId") int orderId,
                                                @QueryParam("orderProductIds") String orderProductIds,@QueryParam("vendorIssue") String vendorIssue){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        VendorUtil vendorUtil=new VendorUtil();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        try{
            handleServiceResponse.setResult(vendorUtil.saveVendorIssueInHandelsHistory(orderProductIds,orderId,fkAssociateId,vendorIssue,ipAddress,userAgent));
        }catch (Exception exception){
            logger.debug("error while adding vendor instruction from vendor in vendor panel ",exception);
        }
        return handleServiceResponse;
    }
    @GET
    @Path("/v1/handels/vendorinstructionfeed")
    public HandleServiceResponse getVendorInstructionFeed(@Context HttpServletResponse response,@Context HttpServletRequest request,
                                                                    @QueryParam("fkAssociateId") String fkAssociateId){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        VendorUtil vendorUtil=new VendorUtil();
        handleServiceResponse.setResult(vendorUtil.getVendorInstruction(fkAssociateId));
        response.addHeader("token",request.getHeader("token"));
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/handels/addVendorInstruction")
    public HandleServiceResponse saveVendorInstruction(@Context HttpServletResponse response,@Context HttpServletRequest request,
        @QueryParam("fkAssociateId") String fkAssociateId,@QueryParam("orderId") int orderId,
        @QueryParam("orderProductId") String orderProductIds,@QueryParam("message") String vendorIssue){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        VendorUtil vendorUtil=new VendorUtil();
        String ipAddress=request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        OrderMapper orderMapper=new OrderMapper();
        Map<String,List<OrderLogModel>> orderLog=new HashMap<>();
        try{
            vendorUtil.saveVendorIssueInHandelsHistory(orderProductIds,orderId,fkAssociateId,vendorIssue,ipAddress,userAgent);
            orderLog.put("logs",orderMapper.getOrderLog(orderId,"message",fkAssociateId));
            handleServiceResponse.setResult(orderLog);
        }catch (Exception exception){
            logger.error("error while adding vendor instruction from vendor in vendor panel ",exception);
        }

        return handleServiceResponse;
    }

}
