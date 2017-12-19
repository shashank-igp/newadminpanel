package com.igp.handles.endpoints;

import com.igp.handles.mappers.Vendor.HandlesVendorMapper;
import com.igp.handles.response.HandleServiceResponse;
import com.igp.handles.models.Vendor.VendorCountDetail;
import com.igp.handles.utils.Vendor.VendorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shanky on 8/7/17.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Vendor {

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
        handleServiceResponse.setResult(vendorUtil.saveVendorIssueInHandelsHistory(orderProductIds,orderId,fkAssociateId,vendorIssue));
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

}
