package com.igp.admin.Voucher.utils;

import com.igp.admin.Voucher.models.VoucherModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherValidationUtil{

    public List<String> createVoucher(VoucherModel voucherModel){
        return validateVoucher(voucherModel);
    }
    public List<String> updateVoucher(VoucherModel voucherModel){
        return validateVoucher(voucherModel);
    }

    public List<String> deleteVoucher(int id, String modifiedBy){
        List<String> errorList = new ArrayList<>();

        if(id <= 0){
            errorList.add("Invalid voucher id.");
        }
        if("".equals(modifiedBy)){
            errorList.add("Modified by not specified.");
        }
        return errorList;
    }

    private List<String> validateVoucher(VoucherModel voucherModel){
        List<String> errorList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//2018-06-31


        //validate voucher.coupon_type
        int voucherCouponType = voucherModel.getVoucherType();
        int voucherValue = voucherModel.getVoucherValue();
        switch (voucherCouponType){
            case 0 : //% based - validate vouchervalue must be > 0 & <= 100
                if(voucherValue < 0 || voucherValue > 100){
                    errorList.add("Invalid voucher value.");
                }
                break;
            case 1 : //value based - validate vouchervalue must be > 0
                if(voucherValue <= 0){
                    errorList.add("Invalid voucher value.");
                }
                break;
            case 2 : //shipping Waiver - validate shipping_waiver_type must not 0
                if(voucherModel.getShippingWaiverType() == 0){
                    errorList.add("Invalid shipping waiver type.");
                }
                break;
            case 3 : //corporate - need not to validate as it will be created dynamically
                break;
            default : errorList.add("Invalid coupon type for voucher.");
                break;
        }


        //validate newigp_voucher_extra_info.coupon_type
        int vInfoCouponType = voucherModel.getApplicableVoucherType();
        List<String> emailList = voucherModel.getApplicableEmail();
        switch (vInfoCouponType){
            case 0 : //need not to validate
                break;
            case 1 : //email based - validate newigp_voucher_extra_info.email
                if(emailList == null || emailList.isEmpty()){
                    errorList.add("Email(s) not specified in applicable email list.");
                }
                break;
            case 2 : //domain based - validate newigp_voucher_extra_info.email
                if(emailList == null || emailList.isEmpty()){
                    errorList.add("Domain(s) not specified in applicable email list.");
                }
                break;
            default : errorList.add("Invalid coupon type for voucher extra info.");
                break;
        }


        //validate newigp_voucher_extra_info.order_value_check
        int orderValueCheck = voucherModel.getOrderValueCheck();
        switch (orderValueCheck){
            case 0 : //no check for order value
                break;
            case 1: //order level without shipping
            case 2: //order level with shipping
            case 3: //product level
                //validate newigp_voucher_extra_info.total_order_value must be > 0
                if(voucherModel.getOrderValue() < 0){
                    errorList.add("Invalid order value.");
                }
                break;
            default:errorList.add("Invalid order value check type.");
        }


        // Expiry date for voucher must be >= today
        try{
            Date expiryDate = dateFormat.parse(voucherModel.getExpiryDate());
            Date nowDate = dateFormat.parse(dateFormat.format(new Date()));
            if(expiryDate.before(nowDate)){
                errorList.add("Expiry date must be equal to or greater than today.");
            }
        }
        catch (ParseException e){
            errorList.add("Invalid format for expiry date, must be yyyy-MM-dd.");
        }


        if(voucherModel.getId() <= 0){
            // CreateVoucher - createdby must not empty
            String createdBy = voucherModel.getCreatedBy();
            if(createdBy == null || createdBy.isEmpty()){
                errorList.add("Created by not specified.");
            }
        }else {
            // UpdateVoucher - modifiedby must not empty
            String modifiedBy = voucherModel.getCreatedBy();
            if(modifiedBy == null || modifiedBy.isEmpty()){
                errorList.add("Modified by not specified.");
            }
        }

        // Either blacklist or whitelist should have data
        int blackSize = voucherModel.getBlackListPts().size();
        int whiteSize = voucherModel.getWhiteListPts().size();
        //if((blackSize == 0 && whiteSize == 0) || (blackSize > 0 && whiteSize > 0)){
        if((blackSize == 0 && whiteSize == 0)){
            errorList.add("Specify either blackListPts or whiteListPts.");
        }


        // Multiple usage count = 100000 for unlimited usage
        if(voucherModel.getMultipleUsage() <= 0){
            voucherModel.setMultipleUsage(100000);
        }
        return errorList;
    }
}
