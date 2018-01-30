package com.igp.handles.admin.mappers.Mail;

import com.igp.handles.admin.utils.MailUtil.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shanky on 29/1/18.
 */
public class MailMapper {
    private static final Logger logger = LoggerFactory.getLogger(MailMapper.class);
    public boolean sendMailToVendor(){
        boolean result=false;
        MailUtil mailUtil=new MailUtil();
        try{

        }catch (Exception exception){
            logger.error("error occured while sending mail to vendor ",exception);
        }

        return result;
    }
}
