package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.DashboardMapper;
import com.igp.handles.admin.models.DashboardDetail;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shanky on 15/1/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Dashboard {
    @GET
    @Path("/v1/admin/handels/getDashboardDetail")
    public HandleServiceResponse getDashboardDetail(@Context HttpServletResponse response,@Context HttpServletRequest request,
        @DefaultValue("1") @QueryParam("scopeId") String scopeId,@QueryParam("specificDate") String date){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        DashboardMapper dashboardMapper=new DashboardMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date specificDate = null;
        DashboardDetail dashboardDetail=new DashboardDetail();
        try {
            specificDate = (date.equals("0") ? null : dateFormat.parse(date));
            dashboardDetail=dashboardMapper.getDashboardOrderCountDetail(specificDate);
            response.addHeader("token",request.getHeader("token"));
            handleServiceResponse.setResult(dashboardDetail);
        }catch (Exception exception){

        }

        return handleServiceResponse;
    }


}
