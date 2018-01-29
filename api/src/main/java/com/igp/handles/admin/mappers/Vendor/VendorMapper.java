package com.igp.handles.admin.mappers.Vendor;

import com.igp.handles.admin.endpoints.Vendor;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 26/1/18.
 */
public class VendorMapper {
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);

    public List<VendorInfoModel> getVendorList(int pincode,int shippingType){
        List<VendorInfoModel> vendorInfoModelList=new ArrayList<>();
         VendorUtil vendorUtil=new VendorUtil();
        try {
            vendorInfoModelList=vendorUtil.getVendorList(pincode,shippingType);
        }catch (Exception exception){
            logger.error("Exception getting vendor List ",exception);
        }
        return vendorInfoModelList;
    }
}
