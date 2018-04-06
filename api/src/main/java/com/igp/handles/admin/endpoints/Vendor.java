package com.igp.handles.admin.endpoints;

import com.igp.handles.admin.mappers.Vendor.VendorMapper;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.vendorpanel.response.HandleServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by shanky on 26/1/18.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Vendor {
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);
    @GET
    @Path("/v1/admin/handels/getVendorList")
    public HandleServiceResponse getVendorList(@DefaultValue("0")@QueryParam("pincode") int pincode,
                                            @DefaultValue("0")@QueryParam("shippingType") int shippingType){
        HandleServiceResponse handleServiceResponse=new HandleServiceResponse();
        VendorMapper vendorMapper=new VendorMapper();
        try {
            if(shippingType==4){
                shippingType=1;
            }
            List<VendorInfoModel> vendorInfoModelList=vendorMapper.getVendorList(pincode,shippingType);
            handleServiceResponse.setResult(vendorInfoModelList);
        }catch (Exception exception){
            logger.error("Exception getting vendor List ",exception);
        }
        return handleServiceResponse;
    }
}
