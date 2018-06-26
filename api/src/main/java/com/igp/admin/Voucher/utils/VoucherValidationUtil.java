package com.igp.admin.Voucher.utils;

import com.igp.admin.Voucher.models.VoucherModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherValidationUtil{

    private List<String> validate(VoucherModel voucherModel){
        List<String> errorList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //TODO
        //validate voucher.coupon_type
        int voucherCouponType = voucherModel.getVoucherType();
        switch (voucherCouponType){
            case 0 : //% based - validate vouchervalue must be > 0 & <= 100
                break;
            case 1 : //value based - validate vouchervalue must be > 0
                break;
            case 2 : //shipping Waiver - validate shipping_waiver_type must not 0
                break;
            case 3 : //corporate - need not validate as it will be created dynamically
                break;
            default : errorList.add("Invalid coupon type for voucher.");
                break;
        }

        //TODO
        //validate newigp_voucher_extra_info.coupon_type
        int vInfoCouponType = voucherModel.getApplicableVoucherType();
        switch (vInfoCouponType){
            case 0 : //need not to validate
                break;
            case 1 : //email based - validate newigp_voucher_extra_info.email
                break;
            case 2 : //domain based - validate newigp_voucher_extra_info.email
                break;
            default : errorList.add("Invalid coupon type for voucher extra info.");
                break;
        }

        //TODO
        //validate newigp_voucher_extra_info.order_value_check
        int orderVauleCheck = voucherModel.getOrderValueCheck();
        switch (orderVauleCheck){
            case 0 : //no check for order value
                break;
            case 1: //order level without shipping
            case 2: //order level with shipping
            case 3: //product level
                //validate newigp_voucher_extra_info.total_order_value must be > 0
                break;
            default:errorList.add("Invalid order value check type.");
        }

        //TODO
        // Expiry date for voucher must be >= today
        try{
            Date date = dateFormat.parse(voucherModel.getExpiryDate());
            Date nowDate = new Date();
        }
        catch (ParseException e){
            errorList.add("Invalid format for expiry date, must be yyyy-MM-dd.");
        }


        // CreateVoucher - createdby must not empty
        String createdBy = voucherModel.getCreatedBy();
        if(createdBy == null || createdBy.isEmpty()){
            errorList.add("Created by not specified.");
        }


        // UpdateVoucher - modifiedby must not empty
        String modifiedBy = voucherModel.getCreatedBy();
        if(modifiedBy == null || modifiedBy.isEmpty()){
            errorList.add("Created by not specified.");
        }

        //TODO
        // Either blacklist or whitelist should have data


        //TODO
        // Multiple usage count = 100000 for unlimited usage
        return errorList;
    }

}
