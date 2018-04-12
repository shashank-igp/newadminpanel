package com.igp.handles.admin.mappers.Mail;

import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.admin.utils.MailUtil.MailUtil;
import com.igp.handles.admin.utils.Vendor.VendorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shanky on 29/1/18.
 */
public class MailMapper {
    private static final Logger logger = LoggerFactory.getLogger(MailMapper.class);
    public boolean sendMailToVendor(String mailBody,int vendorId, String subject){
        boolean result=false;
        MailUtil mailUtil=new MailUtil();
        VendorUtil vendorUtil=new VendorUtil();
        try{
            VendorInfoModel vendorInfoModel=vendorUtil.getVendorInfo(vendorId);
            String[] emailId = vendorInfoModel.getEmail().split(",");
            for(int i=0;i<emailId.length;i++) {
                result = mailUtil.sendGenericMail("", subject, mailBody, emailId[i], true);
            }
        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return result;
    }

    public boolean sendSmsToVendor(String smsBody,int vendorId){
        boolean result=false;
        MailUtil mailUtil=new MailUtil();
        String subject="Action Required - IGP";
        VendorUtil vendorUtil=new VendorUtil();
        try{
            VendorInfoModel vendorInfoModel=vendorUtil.getVendorInfo(vendorId);
            result=mailUtil.sendGenericSms(smsBody,vendorInfoModel.getPhone());
        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return result;
    }
}
